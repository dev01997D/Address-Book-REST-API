package com.blz.addressbookrest.model;

import java.time.LocalDate;
import java.util.Objects;

public class Contact {
	public int id;
	public String name;
	public String gender;
	public double salary;
	public LocalDate startDate;

	public Contact(int id, String name, String gender, double salary, LocalDate startDate) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.salary = salary;
		this.startDate = startDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, gender, name, salary, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Contact))
			return false;
		Contact other = (Contact) obj;
		return id == other.id && Objects.equals(gender, other.gender) && Objects.equals(name, other.name)
				&& Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary)
				&& Objects.equals(startDate, other.startDate);
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", gender=" + gender + ", salary=" + salary + ", startDate="
				+ startDate + "]";
	}
}
