/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.mapping.joincolumn.embedded;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.JiraKey;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Marco Belladelli
 */
@SessionFactory
@DomainModel(annotatedClasses = {
		CharArrayToStringInEmbeddedMultipleJoinColumnTest.Vehicle.class,
		CharArrayToStringInEmbeddedMultipleJoinColumnTest.VehicleInvoice.class
})
@JiraKey("HHH-16040")
public class CharArrayToStringInEmbeddedMultipleJoinColumnTest {
	@BeforeAll
	public void setUp(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			Vehicle vehicle = new Vehicle();
			vehicle.setId( 1L );
			vehicle.setStringProp( "2020" );
			vehicle.setIntProp( 2020 );
			session.persist( vehicle );
			VehicleInvoice invoice = new VehicleInvoice();
			invoice.setId( new VehicleInvoiceId( "2020".toCharArray(), 2020 ) );
			invoice.setVehicle( vehicle );
			vehicle.getInvoices().add( invoice );
			session.persist( invoice );
		} );
	}

	@AfterAll
	public void tearDown(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			session.createMutationQuery( "delete from VehicleInvoice" ).executeUpdate();
			session.createMutationQuery( "delete from Vehicle" ).executeUpdate();
		} );
	}

	@Test
	public void testAssociation(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			final VehicleInvoice vehicleInvoice = session.createQuery(
					"from VehicleInvoice",
					VehicleInvoice.class
			).getSingleResult();
			assertEquals( 1L, vehicleInvoice.getVehicle().getId() );
			assertEquals( "2020", vehicleInvoice.getVehicle().getStringProp() );
		} );
	}

	@Test
	public void testInverse(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			final Vehicle vehicle = session.createQuery(
					"from Vehicle",
					Vehicle.class
			).getSingleResult();
			assertEquals( 1, vehicle.getInvoices().size() );
			assertEquals( "2020", new String( vehicle.getInvoices().get( 0 ).getId().getCharArrayProp() ) );
		} );
	}

	@Embeddable
	public static class VehicleInvoiceId implements Serializable {
		@Column(name = "char_array_col")
		private char[] charArrayProp;

		@Column(name = "int_col")
		private int intProp;

		public VehicleInvoiceId() {
		}

		public VehicleInvoiceId(char[] charArrayProp, int intProp) {
			this.charArrayProp = charArrayProp;
			this.intProp = intProp;
		}

		public char[] getCharArrayProp() {
			return charArrayProp;
		}

		public void setCharArrayProp(char[] charArrayProp) {
			this.charArrayProp = charArrayProp;
		}

		public int getIntProp() {
			return intProp;
		}

		public void setIntProp(int intProp) {
			this.intProp = intProp;
		}
	}

	@Entity(name = "VehicleInvoice")
	public static class VehicleInvoice {
		@EmbeddedId
		private VehicleInvoiceId id;

		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "char_array_col", referencedColumnName = "string_col", insertable = false, updatable = false)
		@JoinColumn(name = "int_col", referencedColumnName = "int_col", insertable = false, updatable = false)
		private Vehicle vehicle;

		public VehicleInvoiceId getId() {
			return id;
		}

		public void setId(VehicleInvoiceId id) {
			this.id = id;
		}

		public Vehicle getVehicle() {
			return vehicle;
		}

		public void setVehicle(Vehicle vehicle) {
			this.vehicle = vehicle;
		}
	}

	@Entity(name = "Vehicle")
	public static class Vehicle implements Serializable {
		@Id
		private Long id;

		@Column(name = "string_col", nullable = false)
		private String stringProp;

		@Column(name = "int_col", nullable = false)
		private int intProp;

		@OneToMany(mappedBy = "vehicle")
		private List<VehicleInvoice> invoices;

		public Vehicle() {
			this.invoices = new ArrayList<>();
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getStringProp() {
			return stringProp;
		}

		public void setStringProp(String stringProp) {
			this.stringProp = stringProp;
		}

		public int getIntProp() {
			return intProp;
		}

		public void setIntProp(int intProp) {
			this.intProp = intProp;
		}

		public List<VehicleInvoice> getInvoices() {
			return invoices;
		}

		public void setInvoices(List<VehicleInvoice> invoices) {
			this.invoices = invoices;
		}
	}
}
