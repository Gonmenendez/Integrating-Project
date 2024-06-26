package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourImageDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourImage;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.*;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.TourRepositoryJPA;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.ITourService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TourServiceImpl implements ITourService {
    private final TourRepositoryJPA tourRepositoryJPA;
    @Autowired
    private TourCategoryServiceImpl tourCategoryServiceImpl;
    @Autowired
    private TourImageServiceImpl tourImageServiceImpl;

    private final ConversionService conversionService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public TourServiceImpl(TourRepositoryJPA tourRepositoryJPA, TourCategoryServiceImpl tourCategoryServiceImpl, ConversionService conversionService, ModelMapper modelMapper) {
        this.tourRepositoryJPA = tourRepositoryJPA;
        this.tourCategoryServiceImpl = tourCategoryServiceImpl;
        this.conversionService = conversionService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<TourDTO> getAllTour() {
        List<Tour> tours = tourRepositoryJPA.findAll();
        List<TourDTO> toursDTO = tours.stream()
                .filter(Objects::nonNull) // Filtrar elementos nulos
                .map(tour -> {
                    TourDTO tourDTO = conversionService.convert(tour, TourDTO.class);
//
//                    // Verificar si la categoría es nula antes de mapearla
//                    if (tour.getCategories() != null) {
//                        Set<TourCategoryDTO> categoryDTOs = tour.getCategories().stream()
//                                .map(category -> modelMapper.map(category, TourCategoryDTO.class))
//                                .collect(Collectors.toSet());
//                        tourDTO.setCategories(categoryDTOs);
//                    }
//
//                    // Verificar si las imágenes son nulas antes de mapearlas
//                    if (tour.getImages() != null) {
//                        Set<TourImageDTO> imageDTOs = tour.getImages().stream()
//                                .map(image -> modelMapper.map(image, TourImageDTO.class))
//                                .collect(Collectors.toSet());
//                        tourDTO.setImages(imageDTOs);
//                    }

                    return tourDTO;
                })
                .collect(Collectors.toList());

        return toursDTO;
    }
    @Transactional
    public Page<TourDTO> getAllPageTour(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Tour> tourPage = tourRepositoryJPA.findAll(pageRequest);

        return tourPage.map(tour -> {
            TourDTO tourDTO = modelMapper.map(tour, TourDTO.class);

            // Verificar si la categoría es nula antes de mapearla
            if (tour.getCategories() != null) {
                Set<TourCategoryDTO> categoryDTOs = tour.getCategories().stream()
                        .map(category -> modelMapper.map(category, TourCategoryDTO.class))
                        .collect(Collectors.toSet());
                tourDTO.setCategories(categoryDTOs);
            }

            // Verificar si las imágenes son nulas antes de mapearlas
            if (tour.getImages() != null) {
                Set<TourImageDTO> imageDTOs = tour.getImages().stream()
                        .map(image -> modelMapper.map(image, TourImageDTO.class))
                        .collect(Collectors.toSet());
                tourDTO.setImages(imageDTOs);
            }

            return tourDTO;
        });
    }

    @Transactional
    public Optional<TourDTO> getTourById(Long id) throws BadIdRequestException {
        if (id <= 0 || id == null) {
            throw new BadIdRequestException("Invalid id: " + id);
        }
        Optional<Tour> tour = tourRepositoryJPA.findById(id);

        if (tour.isPresent()) {
            TourDTO tourDTO = conversionService.convert(tour.get(), TourDTO.class);


            return Optional.ofNullable(tourDTO);
        } else {
            return Optional.empty();
        }
    }


    @Transactional
    public Page<TourDTO> getToursByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Tour> tourPage = tourRepositoryJPA.findByName(name, pageRequest);
        Page<TourDTO> tourDTOPage = tourPage.map(tour -> {
            TourDTO tourDTO = modelMapper.map(tour, TourDTO.class);
            if (tour.getCategories() != null) {
                Set<TourCategoryDTO> categoryDTOs = tour.getCategories().stream()
                        .map(category -> modelMapper.map(category, TourCategoryDTO.class))
                        .collect(Collectors.toSet());
                tourDTO.setCategories(categoryDTOs);
            }
            return tourDTO;
        });
        return tourDTOPage;
    }


    @Transactional
    public Page<TourDTO> getToursByCategory(String category, int page, int size) throws CategoryTitleNotFoundException {
        if (category == null) {
            throw new CategoryTitleNotFoundException("Category cannot be null");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Tour> tourPage = tourRepositoryJPA.findByCategoryTitle(category, pageRequest);

        if (tourPage.isEmpty()) {
            throw new CategoryTitleNotFoundException("No tours found for category: " + category);
        }

        return tourPage.map(tour -> modelMapper.map(tour, TourDTO.class));
    }


    @Transactional
    public List<TourDTO> getRandomTours(int numberOfTours) {
        List<Tour> allTours = tourRepositoryJPA.findAll();
        List<Tour> randomTours = new ArrayList<>();

        if (allTours.size() <= numberOfTours) {
            randomTours.addAll(allTours);
        } else {
            Set<Integer> selectedIndexes = new HashSet<>();
            Random random = new Random();

            while (selectedIndexes.size() < numberOfTours) {
                int randomIndex = random.nextInt(allTours.size());
                selectedIndexes.add(randomIndex);
            }

            for (Integer index : selectedIndexes) {
                randomTours.add(allTours.get(index));
            }
        }

        return randomTours.stream()
                .map(tour -> {
                    TourDTO tourDTO = modelMapper.map(tour, TourDTO.class);

                    // Mapear las imágenes del tour
                    if (tour.getImages() != null) {
                        Set<TourImageDTO> imageDTOs = tour.getImages().stream()
                                .map(image -> modelMapper.map(image, TourImageDTO.class))
                                .collect(Collectors.toSet());
                        tourDTO.setImages(imageDTOs);
                    }

                    // Mapear las categorías del tour
                    if (tour.getCategories() != null) {
                        Set<TourCategoryDTO> categoryDTOs = tour.getCategories().stream()
                                .map(category -> modelMapper.map(category, TourCategoryDTO.class))
                                .collect(Collectors.toSet());
                        tourDTO.setCategories(categoryDTOs);
                    }

                    return tourDTO;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public TourDTO postTour(TourDTO tourDTO) throws TourNameAlreadyExistsException, BadNameRequestException, BadIdRequestException, CategoryTitleNotFoundException, ParseException {
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Verificar si el nombre del tour ya existe
        if (!tourRepositoryJPA.findByName(tourDTO.getName(), pageRequest).isEmpty()) {
            throw new TourNameAlreadyExistsException("The name {" + tourDTO.getName() + "} already exists");
        }

        // Verificar si se proporcionaron categorías
        if (tourDTO.getCategories() != null && !tourDTO.getCategories().isEmpty()) {
            Set<TourCategoryDTO> categoryDTOs = tourDTO.getCategories();
            Set<TourCategoryDTO> updatedCategories = new HashSet<>();

            for (TourCategoryDTO categoryDTO : categoryDTOs) {
                // Verificar si se proporcionó el título de la categoría
                if (categoryDTO.getTitle() != null) {
                    // Obtener la categoría por título
                    Optional<TourCategoryDTO> existingCategoryOpt;
                    try {
                        existingCategoryOpt = tourCategoryServiceImpl.getCategoryByTitle(categoryDTO.getTitle());
                    } catch (CategoryTitleNotFoundException e) {
                        // La categoría no existe, puedes realizar alguna acción o mostrar un mensaje de error
                        existingCategoryOpt = Optional.empty();
                    }

                    if (existingCategoryOpt.isPresent()) {
                        // La categoría ya existe, asignar su ID a la categoría en el tour
                        TourCategoryDTO existingCategoryDTO = existingCategoryOpt.get();
                        updatedCategories.add(existingCategoryDTO);
                    } else {
                        // La categoría no existe, crearla y asignarla al tour
                        TourCategoryDTO createdCategoryDTO = tourCategoryServiceImpl.postCategory(categoryDTO);
                        updatedCategories.add(createdCategoryDTO);
                    }
                } else if (categoryDTO.getId() != null) {
                    // Verificar si se proporcionó el ID de la categoría
                    Optional<TourCategoryDTO> existingCategoryOpt = tourCategoryServiceImpl.getCategoryById(categoryDTO.getId());

                    if (existingCategoryOpt.isPresent()) {
                        // La categoría existe, asignar su ID a la categoría en el tour
                        TourCategoryDTO existingCategoryDTO = existingCategoryOpt.get();
                        updatedCategories.add(existingCategoryDTO);
                    } else {
                        throw new BadIdRequestException("The category id {" + categoryDTO.getId() + "} does not exist");
                    }
                } else {
                    throw new BadIdRequestException("The category id is empty");
                }
            }

            tourDTO.setCategories(updatedCategories);
        }
        // Crear la disponibilidad del tour
        Map<LocalDate, Integer> availability = new HashMap<>();
        if (tourDTO.getAvailability() != null && !tourDTO.getAvailability().isEmpty()) {
            for(Map.Entry<LocalDate, Integer> entry : tourDTO.getAvailability().entrySet()) {
                LocalDate date = entry.getKey();
                availability.put(date, entry.getValue());
            }
        }

        // Mapear y guardar el tour
        Tour tour = modelMapper.map(tourDTO, Tour.class);
        tour.setAvailability(availability);

        Tour savedTour = tourRepositoryJPA.save(tour);

        // Mapear y devolver el tour guardado
        TourDTO savedTourDTO = modelMapper.map(savedTour, TourDTO.class);
        return savedTourDTO;
    }

    @Transactional
    public TourDTO postTourWithImagen(TourDTO tourDTO, MultipartFile imageMain, MultipartFile[] imageFiles) throws TourNameAlreadyExistsException, CategoryTitleNotFoundException, BadIdRequestException, TourHasImagesException {
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Verificar si el nombre del tour ya existe
        if (!tourRepositoryJPA.findByName(tourDTO.getName(), pageRequest).isEmpty()) {
            throw new TourNameAlreadyExistsException("The name {" + tourDTO.getName() + "} already exists");
        }

        // Verificar si se proporcionaron categorías
        if (tourDTO.getCategories() != null && !tourDTO.getCategories().isEmpty()) {
            Set<TourCategoryDTO> categoryDTOs = tourDTO.getCategories();
            Set<TourCategoryDTO> savedCategories = new HashSet<>();

            for (TourCategoryDTO categoryDTO : categoryDTOs) {
                if (categoryDTO.getTitle() != null) {
                    // Obtener la categoría por título
                    Optional<TourCategoryDTO> existingCategoryOpt;
                    try {
                        existingCategoryOpt = tourCategoryServiceImpl.getCategoryByTitle(categoryDTO.getTitle());
                    } catch (CategoryTitleNotFoundException | BadNameRequestException e) {
                        // La categoría no existe, puedes realizar alguna acción o mostrar un mensaje de error
                        existingCategoryOpt = Optional.empty();
                    }

                    if (existingCategoryOpt.isPresent()) {
                        // La categoría ya existe, asignarla al tour
                        savedCategories.add(existingCategoryOpt.get());
                    } else {
                        // La categoría no existe, crearla y asignarla al tour
                        TourCategoryDTO createdCategoryDTO = tourCategoryServiceImpl.postCategory(categoryDTO);
                        savedCategories.add(createdCategoryDTO);
                    }
                } else if (categoryDTO.getId() != null) {
                    // Verificar si se proporcionó el ID de la categoría
                    Optional<TourCategoryDTO> existingCategoryOpt = tourCategoryServiceImpl.getCategoryById(categoryDTO.getId());

                    if (existingCategoryOpt.isPresent()) {
                        // La categoría existe, asignarla al tour
                        savedCategories.add(existingCategoryOpt.get());
                    } else {
                        throw new BadIdRequestException("The category id {" + categoryDTO.getId() + "} does not exist");
                    }
                } else {
                    throw new BadIdRequestException("The category id is empty");
                }
            }

            // Actualizar las categorías del tour
            tourDTO.setCategories(savedCategories);
        }

        // Crear la disponibilidad del tour
        Map<LocalDate, Integer> availability = new HashMap<>();
        if (tourDTO.getAvailability() != null && !tourDTO.getAvailability().isEmpty()) {
            for(Map.Entry<LocalDate, Integer> entry : tourDTO.getAvailability().entrySet()) {
                LocalDate date = entry.getKey();
                availability.put(date, entry.getValue());
            }
        }

        // Mapear y guardar el tour
        Tour tour = modelMapper.map(tourDTO, Tour.class);
        tour.setAvailability(availability);
        Tour tourSave = tourRepositoryJPA.save(tour);
        TourDTO tourSaveDTO = modelMapper.map(tourSave, TourDTO.class);
        // Guardar la imagen en el bucket de AWS S3 utilizando el objeto tour
        List<TourImage> tourImages = tourImageServiceImpl.postImage(tourSaveDTO, imageMain, imageFiles);

        tourSave.setImages(new HashSet<>(tourImages));

        // Mapear y devolver el tour guardado
        TourDTO savedTourDTO = modelMapper.map(tourSave, TourDTO.class);

        return savedTourDTO;
    }

    @Transactional
    public void deleteTourById(Long id) throws BadIdRequestException{
        if(id <= 0 || id == null){
            throw new BadIdRequestException("Invalid id: " + id);
        }
        tourRepositoryJPA.deleteById(id);
    }

    @Transactional
    public List<TourDTO> getToursByAvailability(LocalDate startDate, LocalDate endDate) {
        return tourRepositoryJPA.fintToursByAvailabilityBetween(startDate, endDate)
                .stream()
                .map(tour -> conversionService.convert(tour, TourDTO.class))
                .collect(Collectors.toList());
    }

    public void updateTourLessAvailability(Tour tour, LocalDate date, Integer quantity) throws TourAvailabilityException {
        // Obtener la lista de booking lines asociadas al tour
        Set<BookingLine> bookingLines = new HashSet<>(tour.getBookingLines());

        Map<LocalDate, Integer> availabilityMap = new HashMap<>(tour.getAvailability());

        if (availabilityMap.containsKey(date) && availabilityMap.get(date) >= quantity) {
            // Obtener la disponibilidad actual para la fecha
            Integer availability = availabilityMap.get(date);

            // Restar la cantidad de la booking line a la disponibilidad
            if (availability >= quantity)  {
                availabilityMap.put(date, availability - quantity);
            } else {
                availabilityMap.remove(date);
            }
            // Actualizar la disponibilidad en el tour
            tour.setAvailability(availabilityMap);
        } else {
            throw new TourAvailabilityException("The tour {" + tour.getId() + "} does not have availability for the date {" + date + "}");
        }
        // Guardar el objeto tour en la base de datos
        tourRepositoryJPA.save(tour);
    } //resta la cantidad de disponibilidad

    public void updateTourAdditionAvailability(Tour tour, LocalDate date, Integer quantity) throws TourAvailabilityException {
        // Obtener la lista de booking lines asociadas al tour
        Set<BookingLine> bookingLines = new HashSet<>(tour.getBookingLines());

        Map<LocalDate, Integer> availabilityMap = new HashMap<>(tour.getAvailability());

        if (availabilityMap.containsKey(date)) {
            // Obtener la disponibilidad actual para la fecha
            Integer availability = availabilityMap.get(date);

            // Restar la cantidad de la booking line a la disponibilidad
            if (availability + quantity <=  tour.getQuantity())  {
                availabilityMap.put(date, availability + quantity);
            } else {
                availabilityMap.put(date, tour.getQuantity());
            }
            // Actualizar la disponibilidad en el tour
            tour.setAvailability(availabilityMap);
        } else {
            availabilityMap.put(date, quantity);
            tour.setAvailability(availabilityMap);
        }
        // Guardar el objeto tour en la base de datos
        tourRepositoryJPA.save(tour);
    } //suma la cantidad de disponibilidad

}
