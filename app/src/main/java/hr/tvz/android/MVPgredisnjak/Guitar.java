package hr.tvz.android.MVPgredisnjak;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;


public class Guitar extends BaseModel implements Parcelable {
    @SerializedName("Id")
    Integer id;
    @SerializedName("Name")
    String name;
    @SerializedName("Type")
    String type;
    @SerializedName("NumberOfStrings")
    Integer numberOfStrings;
    @SerializedName("NumberOfFrets")
    Integer numberOfFrets;
    @SerializedName("Price")
    Double price;
    @SerializedName("ImageLink")
    String imageLink;



    public Guitar(Integer id, String name, String type, Integer numberOfStrings, Integer numberOfFrets, Double price, String imageLink) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.numberOfStrings = numberOfStrings;
        this.numberOfFrets = numberOfFrets;
        this.price = price;
        this.imageLink = imageLink;
    }

    public Guitar(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumberOfStrings() {
        return numberOfStrings;
    }

    public void setNumberOfStrings(Integer numberOfStrings) {
        this.numberOfStrings = numberOfStrings;
    }

    public Integer getNumberOfFrets() {
        return numberOfFrets;
    }

    public void setNumberOfFrets(Integer numberOfFrets) {
        this.numberOfFrets = numberOfFrets;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Guitar(Parcel source) {
        List<String> lista = new ArrayList<String>();
        source.readStringList(lista);
        if (lista != null && lista.size() == 7) {
            this.id = Integer.parseInt(lista.get(0));
            this.name = lista.get(1);
            this.type = lista.get(2);
            this.numberOfStrings = Integer.parseInt(lista.get(3));
            this.numberOfFrets = Integer.parseInt(lista.get(4));
            this.price = Double.parseDouble(lista.get(5));
            this.imageLink = lista.get(6);
        }
    }

    @Override
    public int describeContents() {
        return name.hashCode() + type.hashCode() + price.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        List<String> lista = new ArrayList<String>();
        lista.add(id.toString());
        lista.add(name);
        lista.add(type);
        lista.add(numberOfStrings.toString());
        lista.add(numberOfFrets.toString());
        lista.add(price.toString());
        lista.add(imageLink);
        dest.writeStringList(lista);
    }

    public static final Creator<Guitar> CREATOR = new Creator<Guitar>() {
        @Override
        public Guitar createFromParcel(Parcel source) {
            return new Guitar(source);
        }

        @Override
        public Guitar[] newArray(int size) {
            return new Guitar[size];
        }

    };

    @Override
    public String toString() {
        return getName().replace("_"," ");
    }
}
