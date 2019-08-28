package com.p000hl.android.core.helper.animation;

import android.view.animation.Interpolator;

/* renamed from: com.hl.android.core.helper.animation.HLInterpolator */
public class HLInterpolator implements Interpolator, Cloneable {
    private String mEaseType = "";

    public HLInterpolator(String easeType) {
        if (easeType != null) {
            this.mEaseType = easeType;
        }
    }

    public float getInterpolation(float input) {
        float result;
        float result2;
        float result3;
        double s;
        double s2;
        double s3;
        float result4 = input;
        if (this.mEaseType.equals("AnimationEaseType_EaseInQuad")) {
            return (float) Math.pow((double) input, 2.0d);
        }
        if (this.mEaseType.equals("AnimationEaseType_EaseOutQuad")) {
            return (2.0f * input) - ((float) Math.pow((double) input, 2.0d));
        }
        if (this.mEaseType.equals("AnimationEaseType_EaseInOutQuad")) {
            if (((double) input) <= 0.5d) {
                return 2.0f * ((float) Math.pow((double) input, 2.0d));
            }
            return (float) (-0.5d * ((double) ((((2.0f * input) - 1.0f) * ((2.0f * input) - 3.0f)) - 1.0f)));
        } else if (this.mEaseType.equals("AnimationEaseType_EaseInCubic")) {
            return (float) Math.pow((double) input, 3.0d);
        } else {
            if (this.mEaseType.equals("AnimationEaseType_EaseOutCubic")) {
                return ((float) Math.pow((double) (input - 1.0f), 3.0d)) + 1.0f;
            }
            if (this.mEaseType.equals("AnimationEaseType_EaseInOutCubic")) {
                if (((double) input) <= 0.5d) {
                    return (float) (4.0d * Math.pow((double) input, 3.0d));
                }
                return (float) (0.5d * (Math.pow((double) ((2.0f * input) - 2.0f), 3.0d) + 2.0d));
            } else if (this.mEaseType.equals("AnimationEaseType_EaseInQuart")) {
                return (float) Math.pow((double) input, 4.0d);
            } else {
                if (this.mEaseType.equals("AnimationEaseType_EaseOutQuart")) {
                    return (float) (-(Math.pow((double) (input - 1.0f), 4.0d) - 1.0d));
                }
                if (this.mEaseType.equals("AnimationEaseType_EaseInOutQuart")) {
                    if (((double) input) <= 0.5d) {
                        return (float) (8.0d * Math.pow((double) input, 4.0d));
                    }
                    return (float) (-0.5d * (Math.pow((double) ((2.0f * input) - 2.0f), 4.0d) - 2.0d));
                } else if (this.mEaseType.equals("AnimationEaseType_EaseInQuint")) {
                    return (float) Math.pow((double) input, 5.0d);
                } else {
                    if (this.mEaseType.equals("AnimationEaseType_EaseOutQuint")) {
                        return ((float) Math.pow((double) (input - 1.0f), 5.0d)) + 1.0f;
                    }
                    if (this.mEaseType.equals("AnimationEaseType_EaseInOutQuint")) {
                        if (((double) input) <= 0.5d) {
                            return (float) (16.0d * Math.pow((double) input, 5.0d));
                        }
                        return (float) (0.5d * (Math.pow((double) ((2.0f * input) - 2.0f), 5.0d) + 2.0d));
                    } else if (this.mEaseType.equals("AnimationEaseType_EaseInSine")) {
                        return (float) (1.0d - Math.cos((((double) input) * 3.141592653589793d) / 2.0d));
                    } else {
                        if (this.mEaseType.equals("AnimationEaseType_EaseOutSine")) {
                            return (float) Math.sin((((double) input) * 3.141592653589793d) / 2.0d);
                        }
                        if (this.mEaseType.equals("AnimationEaseType_EaseInOutSine")) {
                            return (float) (0.5d - (0.5d * Math.cos(((double) input) * 3.141592653589793d)));
                        }
                        if (this.mEaseType.equals("AnimationEaseType_EaseInExpo")) {
                            return (float) (input == 0.0f ? 0.0d : Math.pow(2.0d, (double) (10.0f * (input - 1.0f))));
                        } else if (this.mEaseType.equals("AnimationEaseType_EaseOutExpo")) {
                            return (float) (input == 1.0f ? 1.0d : (-Math.pow(2.0d, (double) (-10.0f * input))) + 1.0d);
                        } else if (this.mEaseType.equals("AnimationEaseType_EaseInOutExpo")) {
                            if (input == 0.0f) {
                                return 0.0f;
                            }
                            if (((double) input) <= 0.5d) {
                                return (float) (0.5d * Math.pow(2.0d, (double) (10.0f * ((2.0f * input) - 1.0f))));
                            }
                            if (input < 1.0f) {
                                return (float) (0.5d * ((-Math.pow(2.0d, (double) (-10.0f * ((2.0f * input) - 1.0f)))) + 2.0d));
                            }
                            return 1.0f;
                        } else if (this.mEaseType.equals("AnimationEaseType_EaseInCirc")) {
                            return (float) (-(Math.sqrt((double) (1.0f - ((float) Math.pow((double) input, 2.0d)))) - 1.0d));
                        } else {
                            if (this.mEaseType.equals("AnimationEaseType_EaseOutCirc")) {
                                return (float) Math.sqrt(1.0d - Math.pow((double) (input - 1.0f), 2.0d));
                            }
                            if (this.mEaseType.equals("AnimationEaseType_EaseInOutCirc")) {
                                if (((double) input) <= 0.5d) {
                                    return (float) (-0.5d * (Math.sqrt((double) (1.0f - (4.0f * ((float) Math.pow((double) input, 2.0d))))) - 1.0d));
                                }
                                return (float) (0.5d * (Math.sqrt(1.0d - Math.pow((double) ((2.0f * input) - 2.0f), 2.0d)) + 1.0d));
                            } else if (this.mEaseType.equals("AnimationEaseType_EaseInElastic")) {
                                double a = 1.0d;
                                if (input == 0.0f) {
                                    return 0.0f;
                                }
                                if (input == 1.0f) {
                                    return 1.0f;
                                }
                                if (1.0d < 1.0d) {
                                    a = 1.0d;
                                    s3 = 0.3d / 4.0d;
                                } else {
                                    s3 = (0.3d / 6.283185307179586d) * Math.asin(1.0d / 1.0d);
                                }
                                return (float) (-(Math.pow(2.0d, (double) (10.0f * (input - 1.0f))) * a * Math.sin(((((double) (input - 1.0f)) - s3) * 6.283185307179586d) / 0.3d)));
                            } else if (this.mEaseType.equals("AnimationEaseType_EaseOutElastic")) {
                                double a2 = 1.0d;
                                if (input == 0.0f) {
                                    return 0.0f;
                                }
                                if (input == 1.0f) {
                                    return 1.0f;
                                }
                                if (1.0d < 1.0d) {
                                    a2 = 1.0d;
                                    s2 = 0.3d / 4.0d;
                                } else {
                                    s2 = (0.3d / 6.283185307179586d) * Math.asin(1.0d / 1.0d);
                                }
                                return (float) ((Math.pow(2.0d, (double) (-10.0f * input)) * a2 * Math.sin(((((double) input) - s2) * 6.283185307179586d) / 0.3d)) + 1.0d);
                            } else if (this.mEaseType.equals("AnimationEaseType_EaseInOutElastic")) {
                                double a3 = 1.0d;
                                if (input == 0.0f) {
                                    return 0.0f;
                                }
                                if (input == 1.0f) {
                                    return 1.0f;
                                }
                                if (1.0d < 1.0d) {
                                    a3 = 1.0d;
                                    s = 0.45d / 4.0d;
                                } else {
                                    s = (0.45d / 6.283185307179586d) * Math.asin(1.0d / 1.0d);
                                }
                                if (((double) input) <= 0.5d) {
                                    return (float) (-0.5d * Math.pow(2.0d, (double) (10.0f * ((2.0f * input) - 1.0f))) * a3 * Math.sin(((((double) ((2.0f * input) - 1.0f)) - s) * 6.283185307179586d) / 0.45d));
                                }
                                return (float) ((Math.pow(2.0d, (double) (-10.0f * ((2.0f * input) - 1.0f))) * a3 * Math.sin(((((double) ((2.0f * input) - 1.0f)) - s) * 6.283185307179586d) / 0.45d) * 0.5d) + 1.0d);
                            } else if (this.mEaseType.equals("AnimationEaseType_EaseInBack")) {
                                return (float) (((double) ((float) Math.pow((double) input, 2.0d))) * (((1.0d + 1.70158d) * ((double) input)) - 1.70158d));
                            } else {
                                if (this.mEaseType.equals("AnimationEaseType_EaseOutBack")) {
                                    return (float) ((Math.pow((double) (input - 1.0f), 2.0d) * (((1.0d + 1.70158d) * ((double) (input - 1.0f))) + 1.70158d)) + 1.0d);
                                }
                                if (this.mEaseType.equals("AnimationEaseType_EaseInOutBack")) {
                                    if (((double) input) <= 0.5d) {
                                        double s4 = 1.70158d * 1.525d;
                                        return (float) (0.5d * Math.pow((double) (2.0f * input), 2.0d) * ((((1.0d + s4) * 2.0d) * ((double) input)) - s4));
                                    }
                                    double s5 = 1.70158d * 1.525d;
                                    return (float) (0.5d * ((Math.pow((double) ((2.0f * input) - 2.0f), 2.0d) * (((1.0d + s5) * ((double) ((2.0f * input) - 2.0f))) + s5)) + 2.0d));
                                } else if (this.mEaseType.equals("AnimationEaseType_EaseInBounce")) {
                                    if (((double) (1.0f - input)) < 0.36363636363636365d) {
                                        result3 = (float) (7.5625d * Math.pow((double) (1.0f - input), 2.0d));
                                    } else if (((double) (1.0f - input)) < 0.7272727272727273d) {
                                        result3 = (float) ((7.5625d * Math.pow(((double) (1.0f - input)) - 0.5454545454545454d, 2.0d)) + 0.75d);
                                    } else if (((double) (1.0f - input)) < 0.9090909090909091d) {
                                        result3 = (float) ((7.5625d * Math.pow(((double) (1.0f - input)) - 0.8181818181818182d, 2.0d)) + 0.9375d);
                                    } else {
                                        result3 = (float) ((7.5625d * Math.pow(((double) (1.0f - input)) - 0.9545454545454546d, 2.0d)) + 0.984375d);
                                    }
                                    return 1.0f - result3;
                                } else if (this.mEaseType.equals("AnimationEaseType_EaseOutBounce")) {
                                    if (((double) input) < 0.36363636363636365d) {
                                        return (float) (7.5625d * ((double) ((float) Math.pow((double) input, 2.0d))));
                                    }
                                    if (((double) input) < 0.7272727272727273d) {
                                        return (float) ((7.5625d * Math.pow(((double) input) - 0.5454545454545454d, 2.0d)) + 0.75d);
                                    }
                                    if (((double) input) < 0.9090909090909091d) {
                                        return (float) ((7.5625d * Math.pow(((double) input) - 0.8181818181818182d, 2.0d)) + 0.9375d);
                                    }
                                    return (float) ((7.5625d * Math.pow(((double) input) - 0.9545454545454546d, 2.0d)) + 0.984375d);
                                } else if (!this.mEaseType.equals("AnimationEaseType_EaseInOutBounce")) {
                                    return result4;
                                } else {
                                    if (((double) input) <= 0.5d) {
                                        if (((double) (1.0f - (2.0f * input))) < 0.36363636363636365d) {
                                            result2 = (float) (7.5625d * Math.pow((double) (1.0f - (2.0f * input)), 2.0d));
                                        } else if (((double) (1.0f - (2.0f * input))) < 0.7272727272727273d) {
                                            result2 = (float) ((7.5625d * Math.pow(((double) (1.0f - (2.0f * input))) - 0.5454545454545454d, 2.0d)) + 0.75d);
                                        } else if (((double) (1.0f - (2.0f * input))) < 0.9090909090909091d) {
                                            result2 = (float) ((7.5625d * Math.pow(((double) (1.0f - (2.0f * input))) - 0.8181818181818182d, 2.0d)) + 0.9375d);
                                        } else {
                                            result2 = (float) ((7.5625d * Math.pow(((double) (1.0f - (2.0f * input))) - 0.9545454545454546d, 2.0d)) + 0.984375d);
                                        }
                                        return (float) (0.5d * ((double) (1.0f - result2)));
                                    }
                                    if (((double) ((2.0f * input) - 1.0f)) < 0.36363636363636365d) {
                                        result = (float) (7.5625d * Math.pow((double) ((2.0f * input) - 1.0f), 2.0d));
                                    } else if (((double) ((2.0f * input) - 1.0f)) < 0.7272727272727273d) {
                                        result = (float) ((7.5625d * Math.pow(((double) ((2.0f * input) - 1.0f)) - 0.5454545454545454d, 2.0d)) + 0.75d);
                                    } else if (((double) ((2.0f * input) - 1.0f)) < 0.9090909090909091d) {
                                        result = (float) ((7.5625d * Math.pow(((double) ((2.0f * input) - 1.0f)) - 0.8181818181818182d, 2.0d)) + 0.9375d);
                                    } else {
                                        result = (float) ((7.5625d * Math.pow(((double) ((2.0f * input) - 1.0f)) - 0.9545454545454546d, 2.0d)) + 0.984375d);
                                    }
                                    return (float) (0.5d * ((double) (1.0f + result)));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
