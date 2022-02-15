package com.e.faiagenda;

public class Contact {

    private String birth, telephone, name;

    public Contact(String birth, String telephone, String name) {
        this.birth = birth;
        this.telephone = telephone;
        this.name = name;
    }


    private String dateFormat(String date){
        String[] parts = date.split("-");
        String day, month, year;

        day = parts[2];
        month = parts[1];
        year = parts[0];

        return day+"-"+month+"-"+year;
    }

    public String getBirth() {
        return dateFormat(birth);
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  "Nombre: " + name + " - "
                +"Telefono: "+ telephone + "\n"
                +"Cumpleaños: " + birth;
    }
}
