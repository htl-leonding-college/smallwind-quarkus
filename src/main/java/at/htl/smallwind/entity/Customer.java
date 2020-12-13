package at.htl.smallwind.entity;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;

@Entity
@Table(name = "SW_CUSTOMER")//,
      // indexes = @Index(name = "ABBR_UQ_IDX"))
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUST_ID")
    private Long id;

    @Column(name = "CUST_NAME", length = 40)
    private String name;

    @Column(name = "CUST_COUNTRY", length = 40)
    private String country;

    //@NotNull @Max(5) @Min(5)
    @Column(name = "CUST_ABBR", length = 5)
    private String abbr;

    @Column(name = "CUST_CCARD_NO", length = 16)
    private String ccardNo;

    public Customer() {
    }


    public Customer(String name, String country, String abbr, String ccardNo) {
        this.name = name;
        this.country = country;
        this.abbr = abbr;
        this.ccardNo = ccardNo;
    }


    public Customer(String line) {
        String[] elems = line.split(",");
        this.name = elems[0];
        this.country = elems[1];
        this.abbr = elems[2];
        if (elems[3].isBlank()) {
            this.ccardNo = elems[3];
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getCcardNo() {
        return ccardNo;
    }

    public void setCcardNo(String ccardNo) {
        this.ccardNo = ccardNo;
    }

    @Override
    public String toString() {
        return name + " (" + country + ")";
    }

    public JsonObjectBuilder getJsonObjectBuilder() {
        final JsonObjectBuilder jsonObjectBuilder =
                Json.createObjectBuilder()
                        .add("id", this.id)
                        .add("name", this.name)
                        .add("country", this.country)
                        .add("abbr", this.abbr)
                        .add("ccardno", this.ccardNo);
        return jsonObjectBuilder;
    }
}
