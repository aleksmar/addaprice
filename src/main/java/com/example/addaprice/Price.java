package com.example.addaprice;

import java.util.Date;
import java.util.Objects;

public class Price {

    /**
     * Идентификатор в БД.
     */
    private long id;
    /**
     * Код товара.
     */
    private String productCode;
    /**
     * Номер цены.
     */
    private int number;
    /**
     * Номер отдела.
     */
    private int depart;
    /**
     * Начало действия.
     */
    private Date begin;
    /**
     * Конец действия.
     */
    private Date end;
    /**
     * Значение цены в копейках.
     */
    private long value;

    private Price(Builder builder) {
        this.productCode = builder.productCode;
        this.number = builder.number;
        this.depart = builder.depart;
        this.begin = builder.begin;
        this.end = builder.end;
        this.value = builder.value;
    }

    public static class Builder {
        private String productCode;
        private int number;
        private int depart;
        private Date begin;
        private Date end;
        private long value;

        public Builder productCode(String productCode) {
            this.productCode = productCode;
            return this;
        }

        public Builder number(int number) {
            this.number = number;
            return this;
        }

        public Builder depart(int depart) {
            this.depart = depart;
            return this;
        }

        public Builder begin(Date date) {
            this.begin = date;
            return this;
        }

        public Builder end(Date date) {
            this.end = date;
            return this;
        }

        public Builder value(long val) {
            this.value = val;
            return this;
        }

        public Price build() {
            return new Price(this);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return id == price.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
