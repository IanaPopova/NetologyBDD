package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    DashboardPage dashboardPage;
    DataHelper.CardData firstCardData;
    DataHelper.CardData secondCardData;
    int firstCardBalance;
    int secondCardBalance;

            @BeforeEach
            void setup() {
            var loginPage = open("http://localhost:9999", LoginPage.class);
            var authInfo = DataHelper.getAuthInfo();
            var verificationPage = loginPage.validLogin(authInfo);
            var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
            dashboardPage = verificationPage.validVerify(verificationCode);
            firstCardData = DataHelper.getFirstCardData();
            secondCardData = DataHelper.getSecondCardData();
            firstCardBalance = dashboardPage.getBalance(firstCardData);
            secondCardBalance = dashboardPage.getBalance(secondCardData);
        }

            @Test
            void shouldTransferMoneyBetweenOwnCards() {
                int amount = DataHelper.makeValidAmount(firstCardBalance);
                var transferPage = dashboardPage.chooseCardForTransfer(secondCardData);
                transferPage.transferValidAmountInput(String.valueOf(amount), firstCardData);

                var updatedFirstCardBalance = dashboardPage.getBalance(firstCardData);
                var updatedSecondCardBalance = dashboardPage.getBalance(secondCardData);

                assertAll(
                        () -> assertEquals(firstCardBalance - amount, updatedFirstCardBalance, "Баланс первой карты неверен"),
                        () -> assertEquals(secondCardBalance + amount, updatedSecondCardBalance, "Баланс второй карты неверен")
                );
            }

            @Test
            void shouldNotAllowTransferMoreThanBalance() {
                int invalidAmount = DataHelper.makeInvalidAmount(firstCardBalance);
                var transferPage = dashboardPage.chooseCardForTransfer(secondCardData);
                transferPage.makeTransfer(String.valueOf(invalidAmount), firstCardData);

                transferPage.findErrorNotifText("Ошибка! Недостаточно средств на карте.");

                assertAll(
                        () -> assertEquals(firstCardBalance, dashboardPage.getBalance(firstCardData), "Баланс первой карты изменился"),
                        () -> assertEquals(secondCardBalance, dashboardPage.getBalance(secondCardData), "Баланс второй карты изменился")
                );
            }
        }

