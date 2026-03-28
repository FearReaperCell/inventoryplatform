package com.smallbiz.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "suppliers")
public class Supplier {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false, unique = true)
  private String name;

  @Email
  private String email;

  private String phone;

  private String notes;

  protected Supplier() {}

  public Supplier(String name, String email, String phone, String notes) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.notes = notes;
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public String getEmail() { return email; }
  public String getPhone() { return phone; }
  public String getNotes() { return notes; }

  public void setName(String name) { this.name = name; }
  public void setEmail(String email) { this.email = email; }
  public void setPhone(String phone) { this.phone = phone; }
  public void setNotes(String notes) { this.notes = notes; }
}
