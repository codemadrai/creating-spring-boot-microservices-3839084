package com.example.explorecalijpa.web;

import com.example.explorecalijpa.repo.TourRatingRepository;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.explorecalijpa.business.TourRatingService;
import com.example.explorecalijpa.model.TourRating;

import jakarta.validation.Valid;

/**
 * Tour Rating Controller
 *
 * Created by Mary Ellen Bowman
 */
@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {
  private final TourRatingRepository tourRatingRepository;
  private TourRatingService tourRatingService;

  public TourRatingController(TourRatingService tourRatingService, TourRatingRepository tourRatingRepository) {
    this.tourRatingService = tourRatingService;
    this.tourRatingRepository = tourRatingRepository;
  }

  /**
   * Create a Tour Rating.
   *
   * @param tourId
   * @param ratingDto
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RatingDto createTourRating(@PathVariable(value = "tourId") int tourId,
      @RequestBody @Valid RatingDto ratingDto) {
      TourRating rating = tourRatingService.createNew(tourId, ratingDto.getCustomerId(), 
        ratingDto.getScore(), ratingDto.getComment());
      return new RatingDto(rating);
  }

  /**
   * Get all ratings for a specified tour id...
   * 
   * @param tourId
   * @return
   */
  @GetMapping
  public List<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId) {
    List<TourRating> tourRatings = tourRatingService.lookupRatings(tourId);
    return tourRatings.stream().map(RatingDto::new).toList();
  }

  /**
   * Calculate the average Score of a Tour.
   *
   * @param tourId
   * @return the average value.
   */
  @GetMapping("/average")
  public Map<String, Double> getAverage(@PathVariable(value = "tourId") int tourId) {
    return Map.of("average", tourRatingService.getAverageScore(tourId));
  }

  /**
   * Update with a put - here we get a tourId and request body of json 
   *   that models a RatingDto..
   * 
   * @param tourId
   * @param ratingDto
   * @return
   */
  @PutMapping
  public RatingDto updateWithPut(@PathVariable(value="tourId") int tourId,
      @RequestBody @Valid RatingDto ratingDto) {
    return new RatingDto(tourRatingService.update(tourId, 
      ratingDto.getCustomerId(),
      ratingDto.getScore(), 
      ratingDto.getComment())); 
  }


  /**
   * Delete a customers tour rating...
   * 
   * @param tourId
   * @param customerId
   */
  @DeleteMapping("{customerId}")
  public void delete(@PathVariable (value="tourId") int tourId, @PathVariable(value="customerId") int customerId) {
    tourRatingService.delete(tourId, customerId);
  }

  /**
   * Handles the NoSuchElementException with a 404 return error
   * 
   */
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String return404(NoSuchElementException exception) {
    return exception.getMessage();
  }
}
