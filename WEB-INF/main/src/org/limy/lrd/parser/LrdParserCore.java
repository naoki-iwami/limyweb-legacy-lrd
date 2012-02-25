/*
 * Created 2007/08/17
 * Copyright (C) 2003-2007  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of limyweb-lrd.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.limy.lrd.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdBlockElement;
import org.limy.lrd.common.LrdElement;
import org.limy.lrd.common.LrdLinkElement;
import org.limy.lrd.common.LrdParentElement;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.LrdTextElement;
import org.limy.lrd.common.LrdTextElement.Type;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.parser.LrdParseSupport.Parsed;
import org.limy.lrd.parser.bean.LrdNonElement;
import org.limy.lrd.parser.bean.html.LrdHtmlElementFactory;

/**
 * @author Naoki Iwami
 */
/* package */ class LrdParserCore {
    
    /**
     * 改行文字
     */
    private static final String BR = "\n";
    
    /**
     * 強調文字などのタグを認識するパターン
     */
    private static final Pattern PATTERN_BLOCKSTR = Pattern.compile(
            "\\(\\(([{*<:])(.*?)[}*>:]\\)\\)");
    
    // ------------------------ Fields

    /** リポジトリ情報 */
    private final LrdRepository repository;
    
    /** Lrdファクトリ */
    private final LrdHtmlElementFactory factory;
    
    /** パースサポート一覧 */
    private final Collection<LrdParseSupport> supports;

    /** ヘッダ部分パースサポート一覧 */
    private final Collection<LrdParseSupport> headerSupports;

    /** Lrdインスタンス */
    private LrdBean bean;
    
    /** 空要素 */
    private LrdElement nonElement;

    /** 現在要素 */
    private LrdElement currElement;

    /** 現在親要素 */
    private LrdBlockElement currParent;
    
    /** 現在属しているブロック */
    private BlockMode blockMode;

    // ------------------------ Constructors

    public LrdParserCore(LrdRepository repository, LrdHtmlElementFactory factory) {
        this.repository = repository;
        this.factory = factory;
        supports = LrdParseSupportFactory.getInstance().getSupports();
        headerSupports = LrdParseSupportFactory.getInstance().getHeaderSupports();
    }

    // ------------------------ Public Methods

    public LrdBean parse(LrdPath path, BufferedReader reader) throws IOException {
        
        // Lrdインスタンス
        LrdBlockElement rootElement = factory.createBlockElement(null, 0);
        bean = new LrdBean(rootElement, path);
        rootElement.setBean(bean);
        nonElement = new LrdNonElement(bean);
        
        // 現在要素
        currElement = nonElement;
        // 現在親要素
        currParent = (LrdBlockElement)bean.getRoot();
        // ブロック
        blockMode = BlockMode.NOTHING;
        
        while (reader.ready()) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            
            switch (blockMode) {
            case NOTHING:
                parseOutOfBlockMode(line);
                break;
            case NORMAL:
                parseNormalMode(line);
                break;
            default:
                break;
            }

        }
        return bean;
    }

    /**
     * 文字列の開始インデントを取得します。
     * @param line 文字列
     * @return 開始インデント
     */
    public int getIndent(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != ' ') {
                return i;
            }
        }
        return line.length();
    }
    
    /**
     * テキスト要素に文字を追加します。
     * @param textElement テキスト要素
     * @param line 文字列
     * @throws IOException I/O例外
     */
    public void appendText(LrdParentElement textElement, String line)
            throws IOException {
        
        // タグ文字列を検索
        Matcher matcher = PATTERN_BLOCKSTR.matcher(line);
        // 処理した最後の文字位置
        int lastPos = 0;
        
        while (matcher.find()) {
            // タグ文字列が存在したら、親テキスト要素に子要素を追加
            
            // まず、通常文字列を追加
            String beforeStr = line.substring(lastPos, matcher.start());
            if (beforeStr.length() > 0) {
                textElement.appendElement(new LrdTextElement(textElement, Type.DEFAULT, beforeStr));
            }
            
            // タグ文字列を追加
            appendBlockStr(textElement, matcher.group(1).charAt(0), matcher.group(2));
            
            lastPos = matcher.end();
        }
        
        // 残りの通常文字列が存在したら、親テキスト要素に追加
        if (lastPos < line.length()) {
            textElement.appendElement(new LrdTextElement(
                    textElement, Type.DEFAULT, line.substring(lastPos)));
        }
        // 改行を追加
        textElement.appendElement(new LrdTextElement(textElement, Type.DEFAULT, BR));
    }

    public void clearCurrElement() {
        currElement = nonElement;
    }

    /**
     * 直前のブロックよりインデントが減っていた場合に親要素を規定の位置まで戻します。
     * @param line 1行
     */
    public void adjustBlock(String line) {
        int nowIndent = getIndent(line);
        int indent = currParent.getIndent();
        
        while (nowIndent < indent) {
            // インデントが減っていたら、親要素から抜ける
            setCurrParent((LrdBlockElement)currParent.getParent());
            indent = currParent.getIndent();
        }
    }

    // ------------------------ Getter/Setter Methods

    /**
     * Lrdファクトリを取得します。
     * @return Lrdファクトリ
     */
    public LrdHtmlElementFactory getFactory() {
        return factory;
    }

    /**
     * 現在要素を取得します。
     * @return 現在要素
     */
    public LrdElement getCurrElement() {
        return currElement;
    }

    /**
     * 現在要素を設定します。
     * @param currElement 現在要素
     */
    public void setCurrElement(LrdElement currElement) {
        this.currElement = currElement;
    }

    /**
     * 現在親要素を取得します。
     * @return 現在親要素
     */
    public LrdBlockElement getCurrParent() {
        return currParent;
    }

    /**
     * 現在親要素を設定します。
     * @param currParent 現在親要素
     */
    public void setCurrParent(LrdBlockElement currParent) {
        this.currParent = currParent;
    }
    
    /**
     * Lrdインスタンスを取得します。
     * @return Lrdインスタンス
     */
    public LrdBean getBean() {
        return bean;
    }

    // ------------------------ Private Methods
    
    /**
     * ブロック外での処理を行います。
     * @param line 文字列
     * @throws IOException I/O例外
     */
    private void parseOutOfBlockMode(String line) throws IOException {
        if ("=begin".equals(line)) {
            blockMode = BlockMode.NORMAL;
        }
        
        for (LrdParseSupport support : headerSupports) {
            support.parse(this, line);
        }
    }

    /**
     * 通常ブロックの処理を行います。
     * @param line 文字列
     * @throws IOException I/O例外
     */
    private void parseNormalMode(String line) throws IOException {
        
        // ブロック終了判定
        if ("=end".equals(line)) {
            blockMode = BlockMode.NOTHING;
            return;
        }

        for (LrdParseSupport support : supports) {
            if (support.parse(this, line) == Parsed.YES) {
                return;
            }
        }
        
    }
    
    /**
     * テキスト要素にタグ文字列を追加します。
     * @param textElement テキスト要素
     * @param tagChar タグ文字（ *,<,{ のいずれか）
     * @param innerText タグ内文字列
     */
    private void appendBlockStr(
            LrdParentElement textElement, char tagChar, String innerText) {
        
        switch (tagChar) {
        case '{':
            textElement.appendElement(new LrdTextElement(textElement, Type.VARIABLE, innerText));
            break;
        case '*':
            textElement.appendElement(new LrdTextElement(textElement, Type.STRONG, innerText));
            break;
        case ':':
            textElement.appendElement(new LrdTextElement(textElement, Type.PLAIN, innerText));
            break;
        case '<':
            appendLinkElement(textElement, innerText);
            break;
        default:
            throw new IllegalArgumentException("Not Support Expession : " + tagChar);
        }
    }

    /**
     * テキスト要素内にリンク要素を追加します。
     * @param textElement 挿入先テキスト要素
     * @param mainText 文字列
     */
    private void appendLinkElement(LrdParentElement textElement, String mainText) {
        String[] splitTexts = mainText.split("\\|");
        if (splitTexts.length == 2) {
            // 表示文字列とリンク先を別々に指定した場合
            String viewText = splitTexts[0]; // 表示文字列
            String href = splitTexts[1]; // リンク先
            if (href.endsWith(",U")) {
                href = href.substring(0, href.length() - 2);
                LrdLinkElement linkElement = new LrdLinkElement(
                        repository, textElement, href, viewText);
                linkElement.setShowDate(true);
                textElement.appendElement(linkElement);
            } else {
                LrdLinkElement linkElement = new LrdLinkElement(
                        repository, textElement, href, viewText);
                if (href.indexOf(',') >= 0) {
                    setSpecifyTarget(linkElement, href);
                }
                textElement.appendElement(linkElement);
            }
        } else {
            // 表示文字列とリンク先を一緒に指定した場合
            LrdLinkElement linkElement = new LrdLinkElement(
                    repository, textElement, mainText);
            String href = mainText;
            if (!mainText.startsWith("\"") && href.indexOf(',') >= 0) {
                // 特定ターゲット名,href 形式の場合
                setSpecifyTarget(linkElement, href);
            }
            textElement.appendElement(linkElement);
        }
    }

    /**
     * カンマで区切られたHREFをリンク要素にセットします（特定ターゲット指定方式）。
     * @param linkElement リンク要素
     * @param href HREF（,で区切られたもの）
     */
    private void setSpecifyTarget(LrdLinkElement linkElement, String href) {
        String url = href.substring(0, href.indexOf(',')); // 「,」以前の文字列を取得
        if (url.startsWith("URL:")) {
            url = url.substring(4); // 「URL:」は取り除く
        }
        linkElement.setSpecifyTarget(url); // 特定ターゲットを指定
        
        String str = href.substring(href.indexOf(',') + 1); // 「,」以降の文字列を取得
        linkElement.setHref(str);
        linkElement.setText(str);
    }


}
