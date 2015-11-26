/*
 * package com.shopping.vindoshop.util;
 * 
 * import java.util.List;
 * 
 * import org.hibernate.transform.ResultTransformer;
 * 
 * import com.shopping.vindoshop.model.Offer; import
 * com.shopping.vindoshop.model.Outlet;
 * 
 * public class OutletTransformer implements ResultTransformer {
 * 
 * @Override//the same order in projection list properties is the same returned
 * by data array... public Outlet transformTuple(Object[]data,String[]alias) {
 * Outlet outlet = new Outlet();
 * 
 * new Outlet((Integer)data[0],new Offer((Integer)data[1]));}
 * 
 * @Override public List transformList(List dogs){return dogs;}//nothing to do
 * here.... }}
 */