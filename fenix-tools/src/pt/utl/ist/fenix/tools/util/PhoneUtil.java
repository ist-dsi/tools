package pt.utl.ist.fenix.tools.util;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneUtil {
    private static final Collection<PhoneNumberType> FIXED_NUMBERS;
    private static final Collection<PhoneNumberType> MOBILE_NUMBERS;

    static {
	FIXED_NUMBERS = new ArrayList<PhoneNumberType>();
	FIXED_NUMBERS.add(PhoneNumberType.VOIP);
	FIXED_NUMBERS.add(PhoneNumberType.FIXED_LINE);

	MOBILE_NUMBERS = new ArrayList<PhoneNumberType>();
	MOBILE_NUMBERS.add(PhoneNumberType.MOBILE);
    }

    private static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();
    private static final String COUNTRY_CODE = "PT";

    private static boolean isExtension(String numberText) {
	return !StringUtils.isEmpty(numberText) && numberText.length() == 4 && StringUtils.isNumeric(numberText);
    }

    public static PhoneNumber getPhoneNumber(String numberText) {
	if (!StringUtils.isEmpty(numberText)) {

	    if (numberText.startsWith("00") && !isExtension(numberText)) {
		numberText = numberText.replaceFirst("00", "+");
	    }

	    try {
		final PhoneNumber phoneNumber = PHONE_UTIL.parse(numberText, COUNTRY_CODE);
		if (PHONE_UTIL.isValidNumber(phoneNumber)) {
		    return phoneNumber;
		}
	    } catch (NumberParseException e) {
		System.out.println("O número não é válido:" + e);
		return null;
	    }
	}
	return null;
    }

    private static PhoneNumberType getPhoneNumberType(PhoneNumber phoneNumber) {
	return phoneNumber != null ? PHONE_UTIL.getNumberType(phoneNumber) : null;
    }

    public static boolean isValidNumber(String numberText) {

	if (isExtension(numberText)) {
	    return true;
	}

	final PhoneNumber phoneNumber = getPhoneNumber(numberText);
	return phoneNumber != null;
    }

    private static boolean isType(PhoneNumberType type, Collection<PhoneNumberType> types) {
	return types.contains(type);
    }

    public static boolean isMobileNumber(String numberText) {
	return isType(getPhoneNumberType(getPhoneNumber(numberText)), MOBILE_NUMBERS);
    }

    public static boolean isFixedNumber(String numberText) {
	return isType(getPhoneNumberType(getPhoneNumber(numberText)), FIXED_NUMBERS);
    }

    public static String getInternacionalFormatNumber(String numberText) {

	if (isExtension(numberText)) {
	    return null; // TODO: convert to number
	}

	final PhoneNumber phoneNumber = getPhoneNumber(numberText);
	if (phoneNumber != null) {
	    return PHONE_UTIL.format(phoneNumber, PhoneNumberFormat.INTERNATIONAL);
	}

	return null;
    }
}
