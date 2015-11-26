package com.shopping.vindoshop.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

class CustomSqlDateEditor extends CustomDateEditor {

	public CustomSqlDateEditor(DateFormat dateFormat, boolean allowEmpty) {
		super(dateFormat, allowEmpty);
	}

	@Override
	public Object getValue() {
		if (super.getValue() != null)
			return new java.sql.Date(
					((java.util.Date) super.getValue()).getTime());
		else
			return null;
	}
}

@ControllerAdvice
public class GlobalBindingInitializer {
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.initDirectFieldAccess();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomSqlDateEditor(
				CommonUtil.pattern, true));
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}
}
