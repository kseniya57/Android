import java.util.Objects;

public class Card {
    int fill, count, shape, color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return fill == card.fill &&
                count == card.count &&
                shape == card.shape &&
                color == card.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fill, count, shape, color);
    }

    @Override
    public String toString() {
        return "Card{" +
                "fill=" + fill +
                ", count=" + count +
                ", shape=" + shape +
                ", color=" + color +
                '}';
    }

    int getThirdCardAttribute(int first, int second) {
        if (first == second) {
            return first;
        } else {
            return 6 - first - second;
        }
    }

    Card getThird(Card secondCard) {
        Card thirdCard = new Card();
        thirdCard.fill = getThirdCardAttribute(fill, secondCard.fill);
        thirdCard.count = getThirdCardAttribute(count, secondCard.count);
        thirdCard.shape = getThirdCardAttribute(shape, secondCard.shape);
        thirdCard.color = getThirdCardAttribute(color, secondCard.color);
        return thirdCard;
    }
}
