package com.usermanagement.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @CreationTimestamp
    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;

    @Column(name = "status")
    private int status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_organization")
    private Organization parentOrganization;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)//relation with User
    @JsonManagedReference
    @JoinColumn(name = "organization_admin", referencedColumnName = "id")
    private User organizationAdmin;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "organization")
    @JsonBackReference
    private List<UserOrganization> userOrganizations;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)//relation with Role
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)//relation with Role
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private User updatedBy;




}
