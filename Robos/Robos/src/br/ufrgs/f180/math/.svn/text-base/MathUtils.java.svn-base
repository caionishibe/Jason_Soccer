package br.ufrgs.f180.math;

import org.apache.log4j.Logger;

/**
 * Auxiliary functions for every mean.
 * 
 * @author Gabriel
 * 
 */
public class MathUtils {

	private static Logger logger = Logger.getLogger(MathUtils.class);

	/**
	 * Makes a three's rule to normalize a value in a 0..1 interval
	 * 
	 * @param min
	 *            lowest saturation point. Values below this will return 0
	 * @param max
	 *            highest saturation point. Values above this will return 1
	 * @param value
	 * @return a double value between 0 and 1
	 * @throws Exception
	 */
	public static double normalize(double min, double max, double value)
			throws Exception {
		return normalize(min, max, value, 0, 1);
	}

	/**
	 * Makes a three's rule to normalize a value in a 0..1 interval
	 * 
	 * @param min
	 *            lowest saturation point. Values below this will return 0
	 * @param max
	 *            highest saturation point. Values above this will return 1
	 * @param value
	 * 
	 * @param start
	 *            beginning of the normalization interval
	 * @param end
	 *            ending of the normalization interval
	 * 
	 * @return a double value between start and end
	 * @throws Exception
	 * 
	 */
	public static double normalize(double min, double max, double value,
			double start, double end) throws Exception {
		if (max <= min)
			throw new Exception(
					"Min cannot be higher than max. Values entered are not valid.");
		if (end <= start)
			throw new Exception(
					"End cannot be higher than start. Values entered are not valid.");
		if (value >= max)
			return end;
		if (value <= min)
			return start;

		double i1 = max - min;
		double i2 = end - start;
		double y = (value - min) * i2 / i1;
		return y + start;
	}

	/**
	 * Returns difference between two angles in Radians. It doesn't make
	 * distinction between clockwise or counterclockwise. In a way the smaller
	 * absolute value will be returned.
	 * 
	 * @param angle1
	 * @param angle2
	 * @return
	 */
	public static double subtractRadians(double angle1, double angle2) {

		double nAngle1 = angle1 % (Math.PI * 2.0);
		double nAngle2 = angle2 % (Math.PI * 2.0);
		double diff1 = nAngle1 - nAngle2;
		double diff2 = diff1 > 0 ? diff1 - (Math.PI * 2.0) : diff1
				+ (Math.PI * 2.0);

		// logger.debug("  Rotation ");
		// logger.debug("    Angle 1: " + nAngle1);
		// logger.debug("    Angle 2: " + nAngle2);
		// logger.debug("    Rotation +: " + diff1);
		// logger.debug("    Rotation -: " + diff2);

		return Math.abs(diff1) < Math.abs(diff2) ? diff1 : diff2;

	}

	public static void main(String[] args) throws Exception {
		logger.debug("0..1  " + normalize(0, 1, 0.5, 0, 1));
		logger.debug("-1..1  " + normalize(0, 1, 0.5, -1, 1));
		logger.debug("1..3  " + normalize(0, 1, 0.5, 1, 3));

		logger.debug("0..1  " + normalize(1, 2, 1.5, 0, 1));
		logger.debug("-1..1  " + normalize(1, 2, 1.5, -1, 1));
		logger.debug("1..3  " + normalize(1, 2, 1.5, 1, 3));

	}
}
