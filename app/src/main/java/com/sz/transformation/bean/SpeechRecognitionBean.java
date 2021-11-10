package com.sz.transformation.bean;

import java.util.List;

/**
 * @author Sean Zhang
 * Date: 2021/11/9
 */
public class SpeechRecognitionBean {

    private String engine_type;
    private List<ResultBean> result;
    private String result_type;
    private Integer scenario_type;

    public String getEngine_type() {
        return engine_type;
    }

    public void setEngine_type(String engine_type) {
        this.engine_type = engine_type;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public String getResult_type() {
        return result_type;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }

    public Integer getScenario_type() {
        return scenario_type;
    }

    public void setScenario_type(Integer scenario_type) {
        this.scenario_type = scenario_type;
    }

    public static class ResultBean {
        private Integer confidence;
        private String ori_word;
        private String pinyin;
        private String word;

        public Integer getConfidence() {
            return confidence;
        }

        public void setConfidence(Integer confidence) {
            this.confidence = confidence;
        }

        public String getOri_word() {
            return ori_word;
        }

        public void setOri_word(String ori_word) {
            this.ori_word = ori_word;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }
}
