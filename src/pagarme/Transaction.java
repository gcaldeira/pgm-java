package pagarme;

import java.util.Collection;
import java.util.Map;

import pagarme.converter.JSonConverter;
import pagarme.exception.InvalidOperationException;
import pagarme.exception.PagarMeException;

import com.google.gson.annotations.SerializedName;

public class Transaction extends PagarMeModel {

    @SerializedName("status")
    private String status;

    @SerializedName("refuse_reason")
    private TransactionRefuseReason refuseReason;

    @SerializedName("amount")
    private int amount;

    @SerializedName("installments")
    private int installments;

    @SerializedName("card_holder_name")
    private String CardHolderName;

    @SerializedName("card_last_digits")
    private String cardLastDigits;

    @SerializedName("card_brand")
    private String cardBrand;

    @SerializedName("postback_url")
    private String postbackUrl;

    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;

    @SerializedName("antifraud_score")
    private Double antifraudScore;

    @SerializedName("boleto_url")
    private String boletoUrl;

    @SerializedName("boleto_barcode")
    private String boletoBarcode;

    @SerializedName("subscription_id")
    private String subscriptionId;

    @SerializedName("customer")
    private Customer customer;

    public Transaction(PagarMeProvider provider) {
        this.provider = provider;
    }

    public Transaction(PagarMeProvider provider, PagarMeQueryResponse result) {
        this.result = result;
        this.provider = provider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TransactionRefuseReason getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(TransactionRefuseReason refuseReason) {
        this.refuseReason = refuseReason;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public String getCardHolderName() {
        return CardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        CardHolderName = cardHolderName;
    }

    public String getCardLastDigits() {
        return cardLastDigits;
    }

    public void setCardLastDigits(String cardLastDigits) {
        this.cardLastDigits = cardLastDigits;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getPostbackUrl() {
        return postbackUrl;
    }

    public void setPostbackUrl(String postbackUrl) {
        this.postbackUrl = postbackUrl;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAntifraudScore() {
        return antifraudScore;
    }

    public void setAntifraudScore(Double antifraudScore) {
        this.antifraudScore = antifraudScore;
    }

    public String getBoletoUrl() {
        return boletoUrl;
    }

    public void setBoletoUrl(String boletoUrl) {
        this.boletoUrl = boletoUrl;
    }

    public String getBoletoBarcode() {
        return boletoBarcode;
    }

    public void setBoletoBarcode(String boletoBarcode) {
        this.boletoBarcode = boletoBarcode;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void refund() throws PagarMeException {
        String json =
                new PagarMeQuery(provider, "POST", String.format("transactions/%s/refund", id))
                        .execute()
                        .getData();
        cloneTransaction(JSonConverter.getAsObject(json, Transaction.class));
    }

    public Collection<Transaction> listAll() throws PagarMeException, InvalidOperationException {
        return JSonConverter.getAsTransactionList(super.listAll(1000, 0).getData());
    }

    public Collection<Transaction> listAllWithPagination(int totalPerPage, int page)
            throws PagarMeException, InvalidOperationException {
        return JSonConverter.getAsTransactionList(super.listAll(totalPerPage, page).getData());
    }

    public Transaction find(int id) throws InvalidOperationException {
        this.id = id;
        Transaction trans = null;
        try {
            trans = JSonConverter.getAsObject(find().getData(), Transaction.class);
        } catch (PagarMeException e) {
            e.printStackTrace();
        }
        cloneTransaction(trans);
        return trans;
    }

    public Transaction capture(String token, Long amount) throws InvalidOperationException {
        return this.capture(token, amount, null);
    }

    public Transaction capture(String token, Long amount, Map<String, String> metadata)
            throws InvalidOperationException {

        Transaction trans = null;
        try {
            PagarMeQuery pagarmeQuery = new PagarMeQuery(provider, "POST",
                    String.format("transactions/%s/capture", token));

            pagarmeQuery.addQuery("amount", String.valueOf(amount));

            if (null != metadata && !metadata.isEmpty()) {
                for (String key : metadata.keySet())
                    pagarmeQuery.addQuery(String.format("metadata[%s]", key),
                            String.valueOf(metadata.get(key)));
            }

            trans = JSonConverter.getAsObject(pagarmeQuery.execute().getData(), Transaction.class);
        } catch (PagarMeException e) {
            e.printStackTrace();
        }
        cloneTransaction(trans);
        return trans;
    }

    public Transaction refresh() {
        Transaction trans = null;
        try {
            trans = JSonConverter.getAsObject(refreshModel().getData(), Transaction.class);
        } catch (PagarMeException e) {
            e.printStackTrace();
        }
        cloneTransaction(trans);
        return trans;
    }

    @Override
    protected void validate() {
        // TODO Auto-generated method stub

    }

    private void cloneTransaction(Transaction trans) {
        this.id = trans.id;
        this.amount = trans.amount;
        this.antifraudScore = trans.antifraudScore;
        this.boletoBarcode = trans.boletoBarcode;
        this.boletoUrl = trans.boletoUrl;
        this.cardBrand = trans.cardBrand;
        this.CardHolderName = trans.CardHolderName;
        this.cardLastDigits = trans.cardLastDigits;
        this.customer = trans.customer;
        this.dateCreated = trans.dateCreated;
        this.installments = trans.installments;
        this.paymentMethod = trans.paymentMethod;
        this.postbackUrl = trans.postbackUrl;
        this.refuseReason = trans.refuseReason;
        this.status = trans.status;
        this.subscriptionId = trans.subscriptionId;
    }
}
