package appcode.custom.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by r028367 on 01/08/2017.
 */

public class ResizableTextView extends CustomTextView {

    public static final int MAX_LINE = 3;

    public ResizableTextView(Context context) {
        super(context);
        defaultApply();
    }

    public ResizableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultApply();
    }

    public ResizableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultApply();
    }

    private void defaultApply() {
        Log.v("DO_RESIZABLE_TEXT", "APPLY");
        makeTextViewResizable(3, "Ver mais", false, "Ver mais", "Ver menos");
    }

    public void makeTextViewResizable(final int maxQLines
            , final String defaultStr, final boolean isResizableTextView, final String strViewMore, final String strViewLess) {
        final AppCompatTextView textView = this;
        if(textView.getTag() == null) {
            textView.setTag(textView.getText());
        }
        /**
         *
         * https://developer.android.com/reference/android/view/ViewTreeObserver.html
         *
         * ViewTreeObserver permite registrar um listener que eh executado quando um elemento
         * da arvore de View foi alterado
         *
         * */
        ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.v("DO_RESIZABLE_TEXT", "GLOBAL_LAYOUT_OBERSER");
                ViewTreeObserver innerVtObserver = textView.getViewTreeObserver();
                innerVtObserver.removeOnGlobalLayoutListener(this);
                SpannableStringBuilder spannableStringBuilder = null;
                int linesOfTextView  = textView.getLineCount();
                /**
                 * https://developer.android.com/reference/android/widget/TextView.html#getLayout()
                 * Recupera a view que e usado para mostrar o TextView
                 * */
                Layout layout           = textView.getLayout();
                CharSequence targetText = textView.getText();
                Log.i("ON_PREDRAWING", String.format("%s", textView.onPreDraw()));
                if(layout != null) {
                    Log.v("DO_RESIZABLE_TEXT", "LAYOUT_EXISTS");
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
                                , lineEndIndex - defaultStr.length() + 1).toString();
                        //
                        String finalString = String.format("%s %s", subStr, defaultStr);
                        textView.setText(finalString);
                        /**
                         * {@link android.text.method.MovementMethod} setMovementMethod
                         * */
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                        subStr = textView.getText().toString();
                        // Spanned spanned = fromHTML(subStr);
                        spannableStringBuilder = addClickablePartTextViewResizable(
                                 subStr
                                , textView
                                , maxQLines
                                , defaultStr
                                , isResizableTextView
                                , strViewMore
                                , strViewLess);
                        textView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
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
                        String subStr = targetText.subSequence(0, lineEndIndex - defaultStr.length() + 1).toString();
                        String finalString = String.format("%s %s", subStr, defaultStr);
                        textView.setText(finalString);
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
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                        subStr = textView.getText().toString();
                        spannableStringBuilder = addClickablePartTextViewResizable(subStr, textView
                                , maxQLines, defaultStr, isResizableTextView, strViewMore, strViewLess);
                        textView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                    }
                    else {
                        linesOfTextView     = layout.getLineCount();
                        int lineEndIndex    = layout.getLineEnd(linesOfTextView - 1);
                        String subStr       = targetText.subSequence(0, lineEndIndex).toString();
                        String finalString = String.format("%s %s", subStr, defaultStr);
                        textView.setText(finalString);
                        /**
                         * {@link android.text.method.MovementMethod} setMovementMethod
                         * */
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                        String text = textView.getText().toString();
                        //Spanned spanned = fromHTML(text);
                        spannableStringBuilder = addClickablePartTextViewResizable(
                                  text
                                , textView
                                , lineEndIndex
                                , defaultStr
                                , isResizableTextView
                                , strViewMore
                                , strViewLess);
                        textView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                    }
                }
            }
        });
    }



    private SpannableStringBuilder addClickablePartTextViewResizable(
             final CharSequence subsetCharSeq
            ,final TextView target
            ,final int maxQLines
            ,final String spanableText
            ,final boolean isResizableTextView
            ,final String strViewMore
            ,final String strViewLess) {
        // Texto reduzido que vai aparecer na View
        String subseqStr =  subsetCharSeq.toString();
        /**
         * {@link SpannableStringBuilder}
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
                    if(isResizableTextView) {
                        makeTextViewResizable(-1, strViewLess, false, strViewMore, strViewLess);
                    }
                    else {
                        makeTextViewResizable(MAX_LINE, strViewMore, true, strViewMore, strViewLess);
                    }
                }
            },start , start + len, 0);
        }
        return spannableStringBuilder;
    }
}
