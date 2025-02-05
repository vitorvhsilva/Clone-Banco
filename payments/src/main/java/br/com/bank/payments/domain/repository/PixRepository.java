package br.com.bank.payments.domain.repository;

import br.com.bank.payments.domain.entity.Pix;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PixRepository extends MongoRepository<Pix, String> {
}
