package com.food.order.system.customer.service.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

/*
 * Normalde parasal işlem gereken tüm yerlerde; Money valueobject'ini kullanmak yerine direkt BigDecimal kullanabiliriz.
 * Parasal işlemler için valueobject kullanmanın bize getirdiği avantajlar var. Değere bir bağlam getiriyor.
 * Money valueobject içerisine baktığımızda içerisinde neleri barındırabileceğini görebiliriz. Diğer bir fayda ise
 * parasal işlemler için business logic'i valueobject'ler içerisine yerleştirebiliriz.
 * */
public class Money {

    private final BigDecimal amount;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    public Money add(Money money) {
        return new Money(setScale(this.amount.add(money.getAmount())));
    }

    public Money substract(Money money) {
        return new Money(setScale(this.amount.subtract(money.getAmount())));
    }

    public Money multiply(int multiplier) {
        return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    /*
     * Bazı fractional(ondalıklı) sayılar tam olarak ifade edilemez. Mesela 7/10 değeri tam ifade edilemez içinde bir noktadan sonra sürekli tekrar eden
     * değerler gelir. İşlemimizi yaparken; bu değer Java'da mevcut bitler kullanılarak en doğru sonuç ile seçilir.Biz işlemlere her devam ettiğimizde
     * hata büyümeye devam eder.Bu hatayı en aza indirmek için yuvarlama modunu Half Even ayarladık.Half Even Kümülatif hataları en aza indirir.
     * */
    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }
}
