package com.shopping.vindoshop.util;

import java.io.Serializable;

/**
 * Entity for Hibernate comunications
 * 
 * @author Miguel Resendiz
 * 
 * @param <I>
 *            Primary key type
 */
public interface Entity<I extends Serializable> extends Serializable {

	/**
	 * Enable poissibility to write generic queries using primary key
	 * 
	 * @return primary key value for entity
	 */
	I getId();

	void setId(I id);

	void setId(String id);

}
