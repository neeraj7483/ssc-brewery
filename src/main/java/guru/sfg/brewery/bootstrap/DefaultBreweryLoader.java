/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.domain.BeerInventory;
import guru.sfg.brewery.domain.BeerOrder;
import guru.sfg.brewery.domain.BeerOrderLine;
import guru.sfg.brewery.domain.Brewery;
import guru.sfg.brewery.domain.Customer;
import guru.sfg.brewery.domain.OrderStatusEnum;
import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.BreweryRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.repository.security.AthorityRepository;
import guru.sfg.brewery.repository.security.UserRespository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import guru.sfg.brewery.web.model.security.Authority;
import guru.sfg.brewery.web.model.security.User;
import lombok.RequiredArgsConstructor;

/**
 * Created by jt on 2019-01-26.
 */
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

	public static final String TASTING_ROOM = "Tasting Room";
	public static final String BEER_1_UPC = "0631234200036";
	public static final String BEER_2_UPC = "0631234300019";
	public static final String BEER_3_UPC = "0083783375213";

	private final BreweryRepository breweryRepository;
	private final BeerRepository beerRepository;
	private final BeerInventoryRepository beerInventoryRepository;
	private final BeerOrderRepository beerOrderRepository;
	private final CustomerRepository customerRepository;
	private final UserRespository userRespository;
	private final AthorityRepository athorityRepository;

	@Override
	public void run(String... args) {
		loadBreweryData();
		loadCustomerData();
		loadUserData();
	}

	private void loadUserData() {
		Authority authorityAdmin = new Authority("ÄDMIN");
		Authority authorityCustomer = new Authority("CUSTOMER");
		Set<Authority> adminAthorities = new HashSet<>();
		Set<Authority> customerAthorities = new HashSet<>();
		adminAthorities.add(authorityAdmin);
		customerAthorities.add(authorityCustomer);

		User admin = new User("spring", "{bcrypt}$2a$10$QQz6T9KPKa6RWR/GlpyCB.4RZORfHhxG0DKiurF65Imj0rfwwD6Sa", true,
				true, true, true, adminAthorities);
		User customer = new User("scot", "{bcrypt}$2a$10$kmOwZXRQV6218/.8xusiyOqAhDm3CPJFecX0lIOnkVahzGJdGtrxy", true,
				true, true, true, customerAthorities);
//		athorityRepository.save(authorityAdmin);
//		athorityRepository.save(authorityCustomer);
		userRespository.save(admin);
		userRespository.save(customer);
	}

	private void loadCustomerData() {
		Customer tastingRoom = Customer.builder().customerName(TASTING_ROOM).apiKey(UUID.randomUUID()).build();

		customerRepository.save(tastingRoom);

		beerRepository.findAll().forEach(beer -> {
			beerOrderRepository.save(BeerOrder.builder().customer(tastingRoom).orderStatus(OrderStatusEnum.NEW)
					.beerOrderLines(Set.of(BeerOrderLine.builder().beer(beer).orderQuantity(2).build())).build());
		});
	}

	private void loadBreweryData() {
		if (breweryRepository.count() == 0) {
			breweryRepository.save(Brewery.builder().breweryName("Cage Brewing").build());

			Beer mangoBobs = Beer.builder().beerName("Mango Bobs").beerStyle(BeerStyleEnum.IPA).minOnHand(12)
					.quantityToBrew(200).upc(BEER_1_UPC).build();

			beerRepository.save(mangoBobs);
			beerInventoryRepository.save(BeerInventory.builder().beer(mangoBobs).quantityOnHand(500).build());

			Beer galaxyCat = Beer.builder().beerName("Galaxy Cat").beerStyle(BeerStyleEnum.PALE_ALE).minOnHand(12)
					.quantityToBrew(200).upc(BEER_2_UPC).build();

			beerRepository.save(galaxyCat);
			beerInventoryRepository.save(BeerInventory.builder().beer(galaxyCat).quantityOnHand(500).build());

			Beer pinball = Beer.builder().beerName("Pinball Porter").beerStyle(BeerStyleEnum.PORTER).minOnHand(12)
					.quantityToBrew(200).upc(BEER_3_UPC).build();

			beerRepository.save(pinball);
			beerInventoryRepository.save(BeerInventory.builder().beer(pinball).quantityOnHand(500).build());

		}
	}
}
