package hu.db.techshop.dao;

import hu.db.techshop.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodDAO {

    List<PaymentMethod> findAll();
    PaymentMethod findById(int id);
    PaymentMethod save(PaymentMethod paymentMethod);
    void delete(PaymentMethod paymentMethod);

}
