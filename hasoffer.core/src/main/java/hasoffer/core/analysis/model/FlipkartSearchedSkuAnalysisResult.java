package hasoffer.core.analysis.model;

import hasoffer.affiliate.model.AffiliateProduct;

/**
 * Created by chevy on 2016/8/12.
 */
public class FlipkartSearchedSkuAnalysisResult {

    private float score;

    private AffiliateProduct ap;

    public FlipkartSearchedSkuAnalysisResult(float score, AffiliateProduct ap) {
        this.score = score;
        this.ap = ap;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public AffiliateProduct getAp() {
        return ap;
    }

    public void setAp(AffiliateProduct ap) {
        this.ap = ap;
    }
}
