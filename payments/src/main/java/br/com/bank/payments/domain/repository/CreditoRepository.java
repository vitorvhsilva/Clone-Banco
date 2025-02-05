package br.com.bank.payments.domain.repository;

import br.com.bank.payments.domain.entity.Credito;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreditoRepository extends MongoRepository<Credito, String> {
}
