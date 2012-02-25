/*
 * Created 2007/08/19
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
package org.limy.lrd.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdElement;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdParentElement;
import org.limy.lrd.parser.bean.LrdChapterElement;
import org.limy.lrd.parser.bean.html.LrdHtmlWritable;

/**
 * Lrd出力用のユーティリティクラスです。
 * @author Naoki Iwami
 */
public final class LrdWriterUtils {

    /** 無視する文字パターン */
    private static final Pattern IGNORE_PATTERN
            = Pattern.compile("<a .*>(Top|Home)</a>.*", Pattern.DOTALL);

    /**
     * private constructor
     */
    private LrdWriterUtils() { }


    /**
     * 旧フォーマットで記述された不必要なChapterを削除します。
     * @param chapter Chapter
     * @param iterator Chapterが所属するイテレータ
     * @param isStartBody BODY開始フラグ
     */
    public static void removeIgnoreChapter(LrdChapterElement chapter,
            Iterator<? extends Object> iterator, boolean isStartBody) {
        
        LrdBean bean = chapter.getBean();
        if (chapter.getText() != null && bean.getTitle() != null) {
            // TOPを検索
            Matcher matcher = IGNORE_PATTERN.matcher(chapter.getText());
            if (matcher.matches()) {
                iterator.remove(); // TOPを削除
            } else if (!isStartBody && chapter.getText().startsWith(bean.getTitle())) {
                iterator.remove(); // ページ名と同じ見出しがBODY開始前に出現したら削除
            }
        }
    }

    /**
     * HTML内容を出力します。
     * @param bean Lrdインスタンス
     * @param out 出力先
     * @throws IOException I/O例外
     */
    public static void writeLrdHtml(LrdBean bean, Appendable out) throws IOException {
        
        LrdElement element = bean.getRoot();
        
        // 不必要なChapter(TOP,HOMEなど)を削る
        boolean isStartBody = false; // BODY開始フラグ
        if (element instanceof LrdParentElement) {
            LrdParentElement block = (LrdParentElement)element;
            for (ListIterator<? extends LrdElement> it = block.listIterator(); it.hasNext();) {
                LrdElement child = it.next();
                if (child instanceof LrdChapterElement) {
                    LrdWriterUtils.removeIgnoreChapter((LrdChapterElement)child, it, isStartBody);
                } else {
                    isStartBody = true; // Chapter以外が出現したらBODY開始とする
                }
            }
        }

        if (element instanceof LrdHtmlWritable) {
            LrdHtmlWritable writableElement = (LrdHtmlWritable)element;
            writableElement.write(out);
        }
    }


    public static void writeVmTemplate(VelocityEngine engine, String vmTemplate,
            Context context, Writer out) throws LrdException {
        
        try {
            Template template = engine.getTemplate(vmTemplate);
            template.merge(context, out);
        } catch (Exception e) {
            throw new LrdException(e);
        }
        
    }

    public static void writeVmTemplate(VelocityEngine engine, Template template,
            Context context, Writer out) throws LrdException {
        
        try {
            template.merge(context, out);
        } catch (Exception e) {
            throw new LrdException(e);
        }
        
    }

}
