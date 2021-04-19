package com.local.security.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.local.common.utils.RandomHelper;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Create-By: yanchen 2021/1/21 19:03
 * @Description: TODO
 */
public class ExchangeCardHelper {

    public enum CardSource {  //卡劵类型
        NX_CARD("NX", "点卷"), MOP_CARD("MOP", "兑换卡");
        private String prefix;
        private String note;

        CardSource(String prefix, String note) {
            this.prefix = prefix;
            this.note = note;
        }
    }

    public static class CardPair<L, R> {
        private L left;    //卡号
        private R right;   //卡密

        private CardPair(L cardNo, R passWorld) {
            this.left = cardNo;
            this.right = passWorld;
        }

        public static <L, R> CardPair of(final L cardNo, final R password) {
            return new CardPair(cardNo, password);
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CardPair that = (CardPair) o;
            return left.equals(that.left);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left);
        }
    }


    /**
     * @param count      个数
     * @param cardSource 兑换卡类型
     * @create-by: yanchen 2021/1/27 22:10
     * @description: 生成兑换卡
     * @return: java.util.Set<com.local.security.util.ExchangeCardHelper.CardPair < java.lang.String, java.lang.String>>
     */
    public static Set<CardPair<String, String>> create(int count, CardSource cardSource) {
        final  int minCount=1;
        if (count < minCount) {
            throw new IllegalArgumentException("生成个数不能小于1");
        }
        return generate(count, cardSource.prefix);
    }

    private static Set<CardPair<String, String>> generate(int count, String cardPrefix) {
        Set<CardPair<String, String>> cards = Sets.newLinkedHashSetWithExpectedSize(count);
        do {
            long cardNo = RandomHelper.oneLong(1_000_000);    //卡号
            String cardPass = RandomHelper.oneString(8, true, true); //卡密
            cards.add(CardPair.of(cardPrefix.concat(String.valueOf(cardNo)), cardPass));
        } while (cards.size() == count);
        return cards;
    }

    //模拟批量查询数据库,校验卡号是否存在
    private static List<ExchangeCard> findDatabase(Set<String> cardNumbers) {
        return ImmutableList.of(
                new ExchangeCard(1, "NX1235679", "dfdfdfdf"),
                new ExchangeCard(2, "NX1235678", "dfdfdfdf"),
                new ExchangeCard(3, "NX12356782", "dfdfdfdf"),
                new ExchangeCard(4, "NX12356784", "dfdfdfdf"),
                new ExchangeCard(5, "NX123567822", "dfdfdfdf"),
                new ExchangeCard(6, "NX123567812", "dfdfdfdf"),
                new ExchangeCard(7, "NX123567834", "dfdfdfdf"),
                new ExchangeCard(8, "NX123567834", "dfdfdfdf"));
    }

    //递归生成有效的兑换卡
    private static Set<CardPair<String, String>> createEffectiveCard(int finalCardCount, CardSource cardSource, Set<CardPair<String, String>> unCheckedCards, Set<CardPair<String, String>> effectiveCards) {
        if (effectiveCards.size() == finalCardCount) {
            return effectiveCards;
        } else {
            final Set<String> unCheckedCardNumbers = unCheckedCards.stream().map((CardPair::getLeft)).collect(Collectors.toSet());
            List<ExchangeCard> database = findDatabase(unCheckedCardNumbers);
            if (!database.isEmpty()) {
                final Set<String> inEffectiveCardNumbers = database.stream().map(ExchangeCard::getCardNo).collect(Collectors.toSet());
                final Set<CardPair<String, String>> checkedCards = unCheckedCards.stream().filter((p) -> !inEffectiveCardNumbers.contains(p.getLeft())).collect(Collectors.toSet());
                final Set<CardPair<String, String>> newUncheckedCards = ExchangeCardHelper.create(finalCardCount - checkedCards.size(), cardSource);
                effectiveCards.addAll(checkedCards);
                createEffectiveCard(finalCardCount, cardSource, newUncheckedCards, effectiveCards);
            } else {
                effectiveCards.addAll(unCheckedCards);
            }
        }
        return effectiveCards;
    }

    public static void main(String[] args) {
        CardSource cardSource = CardSource.NX_CARD;
        final Set<CardPair<String, String>> unCheckedCards = ExchangeCardHelper.create(10, cardSource);
        Set<CardPair<String, String>> effectiveCards = createEffectiveCard(10, cardSource, unCheckedCards, Sets.newHashSetWithExpectedSize(10));
        //TODO 将effectiveCards 进行入库
    }
}

//entity
class ExchangeCard {

    private int id;
    private String cardNo;
    private String cardPass;

    public ExchangeCard(int id, String cardNo, String cardPass) {
        this.id = id;
        this.cardNo = cardNo;
        this.cardPass = cardPass;
    }

    public int getId() {
        return id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardPass() {
        return cardPass;
    }


}
