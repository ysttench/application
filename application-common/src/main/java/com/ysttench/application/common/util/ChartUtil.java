package com.ysttench.application.common.util;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * @author zhengjianfei
 */
public class ChartUtil {
    private static Logger logger = Logger.getLogger(ChartUtil.class);

    private static Font titleFont = new Font("ＭＳ Ｐゴシック", Font.BOLD, 16);
    private static Font middleFont = new Font("ＭＳ Ｐゴシック", Font.PLAIN, 16);
    private static Font smallFont = new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12);

    private static Integer chartMaxRange = 0;

    public static void createPieChart(String title, Map<String, Integer> data, boolean legend, String rootPath) {
        String filePath = getFileName();
        JFreeChart chart = ChartFactory.createPieChart(title, getPieDataset(data), legend, true, false);
        chart.getTitle().setFont(titleFont);
        chart.getLegend().setItemFont(middleFont);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(middleFont);
        saveChart(chart, rootPath + filePath);
    }

    /**
     * 横柱図表の作成
     */
    public static void createBarChartHorizontal(String title, String categoryAxisLabel,
            String valueAxisLabel, Map<String, Integer> data, String rootPath) {
        createBarChart(title, categoryAxisLabel, valueAxisLabel, PlotOrientation.HORIZONTAL, data, rootPath);
    }

    /**
     * 縦柱図表の作成
     */
    public static void createBarChartVertical(String title, String categoryAxisLabel,
            String valueAxisLabel, Map<String, Integer> data, String rootPath) {
        createBarChart(title, categoryAxisLabel, valueAxisLabel, PlotOrientation.VERTICAL, data, rootPath);
    }

    /**
     * 縦柱図表の作成
     */
    public static void createBarChartSubVertical(String title, String categoryAxisLabel,
            String valueAxisLabel, Map<String, Integer> data, String rootPath) {
        createBarChartSub(title, categoryAxisLabel, valueAxisLabel, PlotOrientation.VERTICAL, data, rootPath);
    }

    /**
     * 縦柱図表の作成(複数)
     */
    public static void createBarChartVerticalMore(String title, String categoryAxisLabel,
            String valueAxisLabel, Map<String, Integer> data, String rootPath) {

        Integer maxRange = 0;

        for (Iterator<Entry<String, Integer>> it = data.entrySet().iterator(); it.hasNext();) {
            Entry<String, Integer> entry = it.next();

            if (entry.getValue() > maxRange) {
                maxRange = entry.getValue();
            }

        }

        chartMaxRange = maxRange;
        createBarChartMore(title, categoryAxisLabel, valueAxisLabel, PlotOrientation.VERTICAL, data, rootPath);
    }

    /**
     * 柱形図の作成(複数)
     */
    private static void createBarChartMore(String title, String categoryAxisLabel,
            String valueAxisLabel, PlotOrientation orientation, Map<String, Integer> data, String rootPath) {

        Map<String, Integer> rec = null;
        Integer index = 1;
        Integer i = 0;

        for (Iterator<Entry<String, Integer>> it = data.entrySet().iterator(); it.hasNext();) {
            Entry<String, Integer> entry = it.next();
            i++;
            if (index == 1) {
                rec = new LinkedHashMap<String, Integer>();
            }

            rec.put(entry.getKey(), entry.getValue());

            if (index >= 6 || i == data.size()) {
                createBarChartVertical(title, categoryAxisLabel, valueAxisLabel, rec, rootPath);
                index = 0;
            }

            index++;
        }
    }

    /**
     * 柱形図の作成
     */
    private static void createBarChart(String title, String categoryAxisLabel, String valueAxisLabel,
            PlotOrientation orientation, Map<String, Integer> data, String rootPath) {

        // 柱形図の作成
        JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel,
                getCategoryDataset(data), orientation, false, true, false);

        // 図の名前
        String filePath = getFileName();

        // 図のタイトル
        chart.getTitle().setFont(titleFont);

        /* 　----　詳細設定　----　 */
        CategoryPlot plot = chart.getCategoryPlot();

        // 柱について属性の設定
        BarRenderer renderer = new BarRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelFont(smallFont);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setMaximumBarWidth(0.1); // 柱幅
        plot.setRenderer(renderer);

        /* 　X軸　 */
        CategoryAxis domainAxis = plot.getDomainAxis();
        // X軸のタイトルの文字フォント
        domainAxis.setLabelFont(middleFont);
        // X軸の座標上の文字フォント
        domainAxis.setTickLabelFont(smallFont);

        /* 　Y軸　 */
        ValueAxis rangeAxis = plot.getRangeAxis();
        // Y軸のタイトルの文字フォント
        rangeAxis.setLabelFont(middleFont);
        // Y軸の座標上の文字フォント
        rangeAxis.setTickLabelFont(smallFont);
        rangeAxis.setAutoRangeMinimumSize(1);
        // Y軸の一番高い Itemと図間の距離
        rangeAxis.setUpperMargin(0.1);
        // Y軸の一番小さい値
        rangeAxis.setLowerBound(0);

        if (chartMaxRange != 0) {
            rangeAxis.setAutoRange(false);// 是否自动适应范围
            rangeAxis.setRange(0, chartMaxRange + Math.round(chartMaxRange * 5 / 2));
        }

        /* 　----　詳細設定　----　 */

        // 図の保存
        saveChart(chart, rootPath + filePath);
    }

    /**
     * 柱形図の作成
     */
    private static void createBarChartSub(String title, String categoryAxisLabel, String valueAxisLabel,
            PlotOrientation orientation, Map<String, Integer> data, String rootPath) {

        // 図の名前
        String filePath = getFileName();

        // 柱形図の作成
        JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel,
                getCategoryDataset(data), orientation, false, true, false);

        // 図のタイトル
        chart.getTitle().setFont(titleFont);

        /* 　----　詳細設定　----　 */
        CategoryPlot plot = chart.getCategoryPlot();

        BarRenderer renderer = new BarRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelFont(smallFont);
        renderer.setBaseItemLabelsVisible(true);
        plot.setRenderer(renderer);

        /* 　X軸　 */
        CategoryAxis domainAxis = plot.getDomainAxis();
        // X軸のタイトルの文字フォント
        domainAxis.setLabelFont(middleFont);
        // X軸の座標上の文字フォント
        domainAxis.setTickLabelFont(smallFont);

        if (data.size() >= 5) {
            // X軸のラベルの表現する角度
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        }

        /* 　Y軸　 */
        ValueAxis rangeAxis = plot.getRangeAxis();
        // Y軸のタイトルの文字フォント
        rangeAxis.setLabelFont(middleFont);
        // Y軸の座標上の文字フォント
        rangeAxis.setTickLabelFont(smallFont);
        rangeAxis.setAutoRangeMinimumSize(1);
        // Y軸の一番高い Itemと図間の距離
        rangeAxis.setUpperMargin(0.1);
        // Y軸の一番小さい値
        rangeAxis.setLowerBound(0);
        /* 　----　詳細設定　----　 */

        // 図の保存
        saveChartSub(chart, rootPath + filePath);
    }

    private static PieDataset getPieDataset(Map<String, Integer> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Iterator<Entry<String, Integer>> it = data.entrySet().iterator(); it.hasNext();) {
            Entry<String, Integer> entry = it.next();
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return dataset;
    }

    private static CategoryDataset getCategoryDataset(Map<String, Integer> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Iterator<Entry<String, Integer>> it = data.entrySet().iterator(); it.hasNext();) {
            Entry<String, Integer> entry = it.next();
            dataset.setValue(entry.getValue(), "", entry.getKey());
        }
        return dataset;
    }

    /*
     * 図の保存
     */
    private static void saveChart(JFreeChart chart, String path) {
        try {
            OutputStream os = new FileOutputStream(path);
            ChartUtilities.writeChartAsJPEG(os, chart, 650, 400);
        } catch (FileNotFoundException e) {
            logger.error(e, e);
        } catch (IOException e) {
            logger.error(e, e);
        }
    }

    /*
     * 図の保存
     */
    private static void saveChartSub(JFreeChart chart, String path) {
        try {
            OutputStream os = new FileOutputStream(path);
            ChartUtilities.writeChartAsJPEG(os, chart, 765, 500);
        } catch (FileNotFoundException e) {
            logger.error(e, e);
        } catch (IOException e) {
            logger.error(e, e);
        }
    }

    private static String getFileName() {
        StringBuilder builder = new StringBuilder();
        builder.append("Chart_");
        DateFormat dataFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        builder.append(dataFormat.format(new Date()));
        builder.append("_");
        builder.append(String.format("%03d", new Random().nextInt(999)));
        builder.append(".jpg");
        return builder.toString();
    }
}
