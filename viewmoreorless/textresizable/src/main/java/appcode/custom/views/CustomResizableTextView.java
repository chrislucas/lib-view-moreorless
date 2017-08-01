package appcode.custom.views;

import android.os.Build;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by r028367 on 28/07/2017.
 */

public class CustomResizableTextView {

    public static class CustomClickableSpan extends ClickableSpan {
        private boolean underline;
        public CustomClickableSpan() {
            super();
            this.underline = true;
        }

        public CustomClickableSpan(boolean underline) {
            this.underline = underline;
        }
        /**
         * Performs the click action associated with this span.
         *
         * @param widget
         */
        @Override
        public void onClick(View widget) {

        }
        /**
         * Makes the text underlined and in the link color.
         *
         * @param textPaint
         */
        @Override
        public void updateDrawState(TextPaint textPaint) {
            //super.updateDrawState(ds);
            textPaint.setUnderlineText(this.underline);
        }

        /**
         * Returns "this" for most CharacterStyles, but for CharacterStyles
         * that were generated by {@link #wrap}, returns the underlying
         * CharacterStyle.
         */
        @Override
        public CharacterStyle getUnderlying() {
            return super.getUnderlying();
        }
    }


    public interface CustomCallback<V extends View> {
        public void update(V view);
    }

    private CustomCallback customCallback;

    public CustomResizableTextView() {}

    public CustomResizableTextView(CustomCallback customCallback) {
        this.customCallback = customCallback;
    }


    public <T extends TextView> void test(
              final T target
            , int maxQLines
            , String textViewVisible
            , boolean hasViewMore
            , String strViewMore
            , String strViewLess) {

        if(target.getTag() == null) {
            target.setTag(target.getText());
        }

        SpannableStringBuilder spannableStringBuilder = null;
        int linesOfTextView  = target.getLineCount();
        /**
         * https://developer.android.com/reference/android/widget/TextView.html#getLayout()
         * Recupera a view que e usado para mostrar o TextView
         * */
        CharSequence targetText = target.getText();
        Log.i("ON_PREDRAWING", String.format("%s", target.onPreDraw()));
        /**
         * TODO
         * rever esse codigo, parece estranho
         * */
        Layout layout = target.getLayout();
        if(maxQLines == 0) {
            int lineEndIndex = layout.getLineEnd(0);
            /**
             * Concatenando o texto com a string de 'ver mais'
             * */
            String subStr = targetText.subSequence(0
                    , lineEndIndex - textViewVisible.length() + 1).toString();
            //
            String finalString = String.format("%s %s", subStr, textViewVisible);
            target.setText(finalString);
            /**
             * {@link android.text.method.MovementMethod} setMovementMethod
             * */
            target.setMovementMethod(LinkMovementMethod.getInstance());
            subStr = target.getText().toString();
            // Spanned spanned = fromHTML(subStr);
            spannableStringBuilder = addClickablePartTextViewResizable2(
                    subStr
                    , target
                    , maxQLines, textViewVisible, hasViewMore, strViewMore, strViewLess);
            target.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        }
        /**
         * TextView.getLineCount
         *
         * # https://developer.android.com/reference/android/widget/TextView.html#getLineCount()
         * Retonar o numero de linhas de texto ou 0 se o layout ainda nao foi construido
         *
         * Layout.getLineEnd(int line)
         * # https://developer.android.com/reference/android/text/Layout.html#getLineEnd(int)
         *  retorna a posicao do ultimo caracter de uma linha especifica
         * */
        else if(maxQLines > 0 && linesOfTextView >= maxQLines) {
            int lineEndIndex = layout.getLineEnd(maxQLines - 1);
            /**
             * pegar uma substring entre idxStart e idxEnd-1
             * idxStart = 0;
             * idxEnd   = indice do ultimo caracter da ultima linhas - o comprimeiro da string auxilizar
             * */
            String subStr = targetText.subSequence(0, lineEndIndex - textViewVisible.length() + 1).toString();
            String finalString = String.format("%s %s", subStr, textViewVisible);
            target.setText(finalString);
            /**
             * {@link android.text.method.MovementMethod} setMovementMethod
             * https://developer.android.com/reference/android/text/method/MovementMethod.html
             *
             * Essa interface prove metodos para pegar o posicionamento do cursor, scrolling
             * e selecao de texto
             *
             * {@link LinkMovementMethod}
             * Especializacao de {@link android.text.method.ScrollingMovementMethod}
             * que implementa {@link android.text.method.MovementMethod}
             *
             * Implementa suporte a cliques em texto-link
             * */
            target.setMovementMethod(LinkMovementMethod.getInstance());
            subStr = target.getText().toString();
            //Spanned spanned = fromHTML(subStr);
            spannableStringBuilder = addClickablePartTextViewResizable2(subStr
                    , target, maxQLines, textViewVisible, hasViewMore, strViewMore, strViewLess);
            target.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        }
        else {
            linesOfTextView     = layout.getLineCount();
            int lineEndIndex    = layout.getLineEnd(linesOfTextView - 1);
            String subStr       = targetText.subSequence(0, lineEndIndex).toString();
            String finalString = String.format("%s %s", subStr, textViewVisible);
            target.setText(finalString);
            /**
             * {@link android.text.method.MovementMethod} setMovementMethod
             * */
            target.setMovementMethod(LinkMovementMethod.getInstance());
            String text = target.getText().toString();
            //Spanned spanned = fromHTML(text);
            spannableStringBuilder = addClickablePartTextViewResizable2(
                      text
                    , target
                    , lineEndIndex
                    , textViewVisible
                    , hasViewMore
                    , strViewMore
                    , strViewLess);
            target.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
        }
    }


    /**
     *
     * @param textViewVisible
     * Link que aparece para usuario escolher se quer ver mais ou menos texto
     * num textview
     * */

    public <T extends TextView> void  makeTextViewResizable(final T target
            , final int maxQLines
            , final String textViewVisible
            , final boolean hasViewMore
            , final String strViewMore
            , final String strViewLess) {
        if(target.getTag() == null) {
            target.setTag(target.getText());
        }
        /**
         *
         * https://developer.android.com/reference/android/view/ViewTreeObserver.html
         *
         * ViewTreeObserver permite registrar um listener que eh executado quando um elemento
         * da arvore de View foi alterado
         *
         * */
        ViewTreeObserver viewTreeObserver = target.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver innerVtObserver = target.getViewTreeObserver();
                innerVtObserver.removeOnGlobalLayoutListener(this);
                SpannableStringBuilder spannableStringBuilder = null;
                int linesOfTextView  = target.getLineCount();
                /**
                 * https://developer.android.com/reference/android/widget/TextView.html#getLayout()
                 * Recupera a view que e usado para mostrar o TextView
                 * */
                Layout layout           = target.getLayout();
                CharSequence targetText = target.getText();
                Log.i("ON_PREDRAWING", String.format("%s", target.onPreDraw()));
                if(layout != null) {
                    /**
                     * TODO
                     * rever esse codigo, parece estranho
                     * */
                    if(maxQLines == 0) {
                        int lineEndIndex = layout.getLineEnd(0);
                        /**
                         * Concatenando o texto com a string de 'ver mais'
                         * */
                        String subStr = targetText.subSequence(0
                                , lineEndIndex - textViewVisible.length() + 1).toString();
                        //
                        String finalString = String.format("%s %s", subStr, textViewVisible);
                        target.setText(finalString);
                        /**
                         * {@link android.text.method.MovementMethod} setMovementMethod
                         * */
                        target.setMovementMethod(LinkMovementMethod.getInstance());
                        subStr = target.getText().toString();
                        // Spanned spanned = fromHTML(subStr);
                        spannableStringBuilder = addClickablePartTextViewResizable(
                              subStr
                            , target
                            , maxQLines, textViewVisible, hasViewMore, strViewMore, strViewLess);
                        target.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                    }
                    /**
                     * TextView.getLineCount
                     *
                     * # https://developer.android.com/reference/android/widget/TextView.html#getLineCount()
                     * Retonar o numero de linhas de texto ou 0 se o layout ainda nao foi construido
                     *
                     * Layout.getLineEnd(int line)
                     * # https://developer.android.com/reference/android/text/Layout.html#getLineEnd(int)
                     *  retorna a posicao do ultimo caracter de uma linha especifica
                     * */
                    else if(maxQLines > 0 && linesOfTextView >= maxQLines) {
                        int lineEndIndex = layout.getLineEnd(maxQLines - 1);
                        /**
                         * pegar uma substring entre idxStart e idxEnd-1
                         * idxStart = 0;
                         * idxEnd   = indice do ultimo caracter da ultima linhas - o comprimeiro da string auxilizar
                         * */
                        String subStr = targetText.subSequence(0, lineEndIndex - textViewVisible.length() + 1).toString();
                        String finalString = String.format("%s %s", subStr, textViewVisible);
                        target.setText(finalString);
                        /**
                         * {@link android.text.method.MovementMethod} setMovementMethod
                         * https://developer.android.com/reference/android/text/method/MovementMethod.html
                         *
                         * Essa interface prove metodos para pegar o posicionamento do cursor, scrolling
                         * e selecao de texto
                         *
                         * {@link LinkMovementMethod}
                         * Especializacao de {@link android.text.method.ScrollingMovementMethod}
                         * que implementa {@link android.text.method.MovementMethod}
                         *
                         * Implementa suporte a cliques em texto-link
                         * */
                        target.setMovementMethod(LinkMovementMethod.getInstance());
                        subStr = target.getText().toString();
                        //Spanned spanned = fromHTML(subStr);
                        spannableStringBuilder = addClickablePartTextViewResizable(subStr
                                , target, maxQLines, textViewVisible, hasViewMore, strViewMore, strViewLess);
                        target.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                    }
                    else {
                        linesOfTextView     = layout.getLineCount();
                        int lineEndIndex    = layout.getLineEnd(linesOfTextView - 1);
                        String subStr       = targetText.subSequence(0, lineEndIndex).toString();
                        String finalString = String.format("%s %s", subStr, textViewVisible);
                        target.setText(finalString);
                        /**
                         * {@link android.text.method.MovementMethod} setMovementMethod
                         * */
                        target.setMovementMethod(LinkMovementMethod.getInstance());
                        String text = target.getText().toString();
                        //Spanned spanned = fromHTML(text);
                        spannableStringBuilder = addClickablePartTextViewResizable(
                                  text
                                , target
                                , lineEndIndex
                                , textViewVisible
                                , hasViewMore
                                , strViewMore
                                , strViewLess);
                        target.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                    }
                }
            }
        });
    }

    private static Spanned fromHTML(String html) {
        Spanned spanned = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        else
            spanned = Html.fromHtml(html);
        return spanned;
    }


    public static final int MAX_LINE = 3;


    private SpannableStringBuilder addClickablePartTextViewResizable(
             String subStr
            ,final TextView target
            ,final int maxQLines
            ,final String spanableText
            ,final boolean viewMore
            ,final String strViewMore
            ,final String strViewLess) {
        return addClickablePartTextViewResizable((CharSequence) subStr
                , target, maxQLines, spanableText, viewMore, strViewMore, strViewLess);
    }


    private SpannableStringBuilder addClickablePartTextViewResizable2(
            final CharSequence subsetCharSeq
            ,final TextView target
            ,final int maxQLines
            ,final String spanableText
            ,final boolean viewMore
            ,final String strViewMore
            ,final String strViewLess) {
        // Texto reduzido que vai aparecer na View
        String subseqStr =  subsetCharSeq.toString();
        /**
         *
         * */
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(subsetCharSeq);
        /**
         * Indice de onde começa a string 'Ver mais' no texto reduzido
         * */
        int start = subseqStr.indexOf(spanableText);
        // Tamanho do texto que se quer que apareca para que o usuario clique e mostre mais do texto reduzido
        // exemplo 'Veja mais ...'
        int len = spanableText.length();
        // O texto reduzido contem a substring 'Str' Exemplo 'Ver mais...'
        if(subseqStr.contains(spanableText)) {
            /**
             * class {@link ClickableSpan}
             *
             * Segundo a documentacao, se um objeto do tipo {@link ClickableSpan}
             * for atrelado a uma substring de uma string num TextView, junto
             * a uma implementacao de MovementMethod como {@link LinkMovementMethod}
             * essa substring podera ser 'selecionavel' quando exibida numa view da tela
             * do usuario. Se esse texto for clicado, o metodo onClick sera executado
             * */
            spannableStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    target.setLayoutParams(target.getLayoutParams());
                    String text = target.getTag().toString();
                    target.setText(text, TextView.BufferType.SPANNABLE);
                    target.invalidate();
                    /**
                     * A aplicacao comeca com a opcao de 'ver mais' ativada (TRUE, se o usuario
                     * clicar na opcao 'ver mais', mostramos o texto completo e trocamos o valor
                     * para 'ver menos' e a variavel boolean de TRUE para FALSE
                     *
                     * */
                    if(viewMore) {
                        test(target, -1, strViewLess, false, strViewMore, strViewLess);
                    }
                    else {
                        test(target, MAX_LINE, strViewMore, true, strViewMore, strViewLess);
                    }
                }
            },start , start + len, 0);
        }
        return spannableStringBuilder;
    }

    private SpannableStringBuilder addClickablePartTextViewResizable(
             final CharSequence subsetCharSeq
            ,final TextView target
            ,final int maxQLines
            ,final String spanableText
            ,final boolean viewMore
            ,final String strViewMore
            ,final String strViewLess) {
        // Texto reduzido que vai aparecer na View
        String subseqStr =  subsetCharSeq.toString();
        /**
         *
         * */
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(subsetCharSeq);
        /**
         * Indice de onde começa a string 'Ver mais' no texto reduzido
         * */
        int start = subseqStr.indexOf(spanableText);
        // Tamanho do texto que se quer que apareca para que o usuario clique e mostre mais do texto reduzido
        // exemplo 'Veja mais ...'
        int len = spanableText.length();
        // O texto reduzido contem a substring 'Str' Exemplo 'Ver mais...'
        if(subseqStr.contains(spanableText)) {
            /**
             * class {@link ClickableSpan}
             *
             * Segundo a documentacao, se um objeto do tipo {@link ClickableSpan}
             * for atrelado a uma substring de uma string num TextView, junto
             * a uma implementacao de MovementMethod como {@link LinkMovementMethod}
             * essa substring podera ser 'selecionavel' quando exibida numa view da tela
             * do usuario. Se esse texto for clicado, o metodo onClick sera executado
             * */
            spannableStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    target.setLayoutParams(target.getLayoutParams());
                    String text = target.getTag().toString();
                    target.setText(text, TextView.BufferType.SPANNABLE);
                    target.invalidate();
                    /**
                     * A aplicacao comeca com a opcao de 'ver mais' ativada (TRUE, se o usuario
                     * clicar na opcao 'ver mais', mostramos o texto completo e trocamos o valor
                     * para 'ver menos' e a variavel boolean de TRUE para FALSE
                     *
                     * */
                    if(viewMore) {
                        makeTextViewResizable(target, -1, strViewLess, false, strViewMore, strViewLess);
                    }
                    else {
                        makeTextViewResizable(target, MAX_LINE, strViewMore, true, strViewMore, strViewLess);
                    }
                }
            },start , start + len, 0);
        }
        return spannableStringBuilder;
    }

    private SpannableStringBuilder addClickablePartTextViewResizable(
             final Spanned spanned
            ,final TextView target
            ,final int maxQLines
            ,final String spanableText
            ,final boolean viewMore
            ,final String strViewMore
            ,final String strViewLess) {
        return addClickablePartTextViewResizable((CharSequence) spanned
                , target, maxQLines, spanableText, viewMore, strViewMore, strViewLess);
    }




}
