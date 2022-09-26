package com.usermanagement.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Organization")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Organization implements Serializable {
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_organization")
    private Organization parentOrganization;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//relation with User
   // @JsonManagedReference sir
    @JoinColumn(name = "organization_admin", referencedColumnName = "id")
    private User organizationAdmin;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "organization")
  //  @JsonBackReference sir
    private List<UserOrganization> userOrganizations;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)//relation with Role
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)//relation with Role
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private User updatedBy;




}
