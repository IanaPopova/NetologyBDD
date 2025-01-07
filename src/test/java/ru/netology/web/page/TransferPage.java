package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement transferPageHeader = $(Selectors.byText("Пополнение карты"));
    private SelenideElement transferAmount = $("[data-test-id=amount] input");
    private SelenideElement fromCard = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorNotif = $("[data-test-id=error-notification] .notification__content");

    public TransferPage() {
        transferPageHeader.shouldBe(visible);
    }

    public DashboardPage transferValidAmountInput(String moneyToTransfer, DataHelper.CardData cardData) {
        makeTransfer(moneyToTransfer, cardData);
        return new DashboardPage();
    }

    public void makeTransfer(String moneyToTransfer, DataHelper.CardData cardData) {
        transferAmount.setValue(moneyToTransfer);
        fromCard.setValue(cardData.getCardNumber());
        transferButton.click();
    }

    public void findErrorNotifText(String expectedText) {
        errorNotif.shouldHave(Condition.text(expectedText), Duration.ofSeconds(10)).shouldBe(visible);
    }


}
