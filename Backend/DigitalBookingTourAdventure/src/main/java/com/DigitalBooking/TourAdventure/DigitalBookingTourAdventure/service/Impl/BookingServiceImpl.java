package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.*;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.*;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.BookingRepositoryJPA;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IBookingLineService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IBookingService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.ITourService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private final BookingRepositoryJPA bookingRepository;

    private final ITourService tourService;
    private final IUserService userService;

    private final IBookingLineService bookingLineService;

    private final ConversionService conversionService;

    @Transactional
    public BookingDTO create(CreationBookingDTO createDto) throws BookingNotFoundException, TourAvailabilityException, UserNotFoundException {
        BookingDTO response = new BookingDTO();
        Booking booking = new Booking();

        User userToSet = (User) userService.loadUserByUsername(createDto.getUser().getEmail());
        createDto.setUser(userService.getUserById(userToSet.getId()));

        Long idBookingExist= checkBookingActive(userToSet);
        System.out.println("idBookingExist: "+idBookingExist);
        if(idBookingExist == 0L){
            booking = conversionService.convert(createDto, Booking.class);
        } else {
            Booking existBooking = bookingRepository.findById(idBookingExist).get();
            booking = conversionService.convert(createDto, Booking.class);

            existBooking.addBookingLine(booking.getBookingLines().get(0));
            booking = existBooking;
        }

        return saveBooking(booking);
    }

    @Override
    public BookingDTO update(Long id, BookingDTO dto) throws BookingNotFoundException, TourAvailabilityException {
        return null;
    }

    @Transactional
    public BookingDTO deleteBookingLine(Long id, Long idBookinLine) throws BookingNotFoundException, TourAvailabilityException, BadIdRequestException, BookingLineNotFoundException {
        Booking booking = bookingRepository.getById(id);
        BookingLine bookingLine = conversionService.convert(bookingLineService.getBookingLineById(idBookinLine), BookingLine.class);
        if(bookingRepository.exists(id)){
                bookingLineService.delete(idBookinLine);

                booking.getBookingLines().removeIf(bl -> bl.getId().equals(idBookinLine));
                this.updateTotalPrice(booking);
                this.updateAvailabilityBeforeDeleteBookingLine(bookingLine);

            }else{
                throw new BookingNotFoundException("Booking not found!");
            }
            return conversionService.convert(bookingRepository.getById(id),BookingDTO.class);
    }

    @Transactional
    public BookingDTO updateBookingLine(Long id, BookingLineDTO dto) throws BookingNotFoundException, TourAvailabilityException {
        if(dto.getId()!=null && !dto.getId().equals(id)) throw new BookingNotFoundException("Booking not found!");
        dto.setId(id);
        if(bookingRepository.exists(id)){
            return saveBooking(conversionService.convert(dto, Booking.class));
        }else{
            throw new BookingNotFoundException("Booking not found!");
        }
    }


    @Transactional
    public void delete(Long id) throws BookingNotFoundException {
        int number = bookingRepository.softDelete(id);
        if(number == 0){
            throw new BookingNotFoundException("Booking not found!");
        }
    }

    @Transactional
    public BookingDTO getById(Long id) throws BookingNotFoundException {
        var booking = bookingRepository.findByIdAndDeleted(id,false);
        if(booking.isPresent()){
            return conversionService.convert(booking.get(),BookingDTO.class);
        }else{
            throw new BookingNotFoundException("Booking not found!");
        }
    }

    @Transactional
    public List<BookingDTO> getAllBooking() {
        var bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(booking -> conversionService.convert(booking, BookingDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BookingDTO> getBookingsByUserName(String userName) throws UserNotFoundException {
        if (userName == null) {
            throw new UserNotFoundException("User not found: " + userName);
        }
        List<Booking> bookings = bookingRepository.findByUser(userName);
        List<BookingDTO> response = bookings.stream().map(booking -> conversionService.convert(booking, BookingDTO.class)).collect(Collectors.toList());
        return response;
    }

    @Transactional
    public Page<BookingDTO> getPurchaseOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookingPage = bookingRepository.findByDeleted(false, pageable);
        return bookingPage.map(booking -> conversionService.convert(booking, BookingDTO.class));
    }



    private BookingDTO saveBooking(Booking booking) throws TourAvailabilityException {
        if (booking == null) throw new IllegalArgumentException();
        if (booking.getBookingLines() != null) {
            for (BookingLine bookingLine : booking.getBookingLines()) {
                try {
                    TourDTO tour = tourService.getTourById(bookingLine.getTour().getId()).get();
                    bookingLine.setTour(conversionService.convert(tour, Tour.class));

                    // Asociar la línea de reserva al tour utilizando el método addBookingLine()
                    Tour tourEntity = conversionService.convert(tour, Tour.class);
                    tourEntity.addBookingLine(bookingLine);
                    bookingLine.setTour(tourEntity);

                } catch (BadIdRequestException e) {
                    throw new RuntimeException(e);
                }
            }
            // Actualizar la disponibilidad del tour segun la ultima bookingline agregada
            if(booking.getBookingLines().get(booking.getBookingLines().size()-1).getId() == null){
                this.updateAvailabilityLastTour(booking);
            }
        }
        this.updateTotalPrice(booking);
        Booking createdBooking =  this.bookingRepository.save(booking);
        for(BookingLine bookingLine: createdBooking.getBookingLines()){
            bookingLine.setBooking(createdBooking);
        }

        return conversionService.convert(createdBooking , BookingDTO.class);
    }

    private Long checkBookingActive(User user) throws UserNotFoundException {
        Long active = 0L;
        List<BookingDTO> bookingCheckActive = this.getBookingsByUserName(user.getEmail());
        if(!bookingCheckActive.isEmpty()){
            for (BookingDTO booking : bookingCheckActive) {
                if(booking.getStatus() != null && booking.getStatus()==true) active = booking.getId();
            }
        }
        return active;
    }

    private void updateTotalPrice(Booking booking) {
        double totalPrice = 0;
        for (BookingLine bookingLine : booking.getBookingLines()) {
            double tourPrice = bookingLine.getTour().getPrice(); // Obtener el precio del tour
            totalPrice += tourPrice; // Sumar el precio del tour al totalPrice
        }
        booking.setTotalPrice(BigDecimal.valueOf(totalPrice)); // Establecer el totalPrice calculado en booking
    }

    private void updateAvailabilityLastTour(Booking booking) throws TourAvailabilityException {
        // Actualizar la disponibilidad del tour según la ultima bookingLine
        try {
            //tour a modificar
            Tour tourUpdateAvailability = booking.getBookingLines().get(booking.getBookingLines().size()-1).getTour();
            //fecha a modificar del tour
            LocalDate date = booking.getBookingLines().get(booking.getBookingLines().size()-1).getDate();
            //cantidad de personas a modificar del tour
            Integer people = booking.getBookingLines().get(booking.getBookingLines().size()-1).getQuantity();
            tourService.updateTourLessAvailability(tourUpdateAvailability, date, people);
        } catch (TourAvailabilityException e) {
            throw new TourAvailabilityException (e.getMessage());
        }
    }

    private void updateAvailabilityBeforeDeleteBookingLine (BookingLine bookingline) throws TourAvailabilityException {
        // Actualizar la disponibilidad del tour según la ultima bookingLine
        try {
            //tour a modificar
            Tour tourUpdateAvailability = bookingline.getTour();
            //fecha a modificar del tour
            LocalDate date = bookingline.getDate();
            //cantidad de personas a modificar del tour
            Integer people = bookingline.getQuantity();
            tourService.updateTourAdditionAvailability(tourUpdateAvailability, date, people);
        } catch (TourAvailabilityException e) {
            throw new TourAvailabilityException (e.getMessage());
        }
    }

}
