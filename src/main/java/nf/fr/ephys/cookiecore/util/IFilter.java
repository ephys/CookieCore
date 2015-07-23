package nf.fr.ephys.cookiecore.util;

/**
 * Interface used to create a filter for methods selecting objects in a list.
 *
 * @param <T> The type of objects to filter
 */
public interface IFilter<T> {

  /**
   * Determines if an object is valid for this filter.
   *
   * @param toValidate An object from the list
   * @return true: the object is kept, false: the object is discarded
   */
  boolean isValid(T toValidate);
}