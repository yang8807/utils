package com.magnify.yutils;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * ���빤����
 *
 * @author ����
 */
public class InputUtil {

	/**
	 * ��TextView���һ��InputFilter
	 *
	 * @param view
	 * @param filter
	 */
	public static void addFilter(TextView view, InputFilter filter) {
		InputFilter[] f0 = view.getFilters();
		ArrayList<InputFilter> f1 = new ArrayList<InputFilter>();
		if (f0 != null && f0.length > 0) {
			f1.addAll(Arrays.asList(f0));
		}
		f1.add(filter);
		InputFilter[] f2 = new InputFilter[f1.size()];
		f1.toArray(f2);
		view.setFilters(f2);
	}

	/**
	 * �ַ�����������������TextView��������ַ�<br>
	 * android:digits ���Զ����������Ч�����������뷨������̲�һ����ʵ��<br>
	 *
	 * @author ����
	 */
	public static class DigitsFilter implements InputFilter {

		protected String digits;

		public DigitsFilter(String digits) {
			if (digits == null || digits.length() == 0)
				throw new RuntimeException("Digits should not be empty!");
			this.digits = digits;
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			char[] accept = digits.toCharArray();
			int i;
			for (i = start; i < end; i++) {
				if (!check(accept, source.charAt(i))) {
					break;
				}
			}

			if (i == end) {
				// It was all OK.
				return null;
			}

			if (end - start == 1) {
				// It was not OK, and there is only one char, so nothing remains.
				return "";
			}

			SpannableStringBuilder filtered = new SpannableStringBuilder(source, start, end);
			i -= start;
			end -= start;

			// Only count down to i because the chars before that were all OK.
			for (int j = end - 1; j >= i; j--) {
				if (!check(accept, source.charAt(j))) {
					filtered.delete(j, j + 1);
				}
			}

			return filtered;
		}

		protected boolean check(char[] accept, char c) {
			for (int i = accept.length - 1; i >= 0; i--) {
				if (accept[i] == c) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * �ַ�������������TextView��������ĳЩ�ַ�
	 * @author ����
	 */
	public static class NonDigitsFilter extends DigitsFilter {

		public NonDigitsFilter(String digits) {
			super(digits);
		}

		@Override
		protected boolean check(char[] accept, char c) {
			return !super.check(accept, c);
		}

	}

	/**
	 * �������������������ʽ���ƿ����������<br>
	 * Ҫע����ǣ�����������ַ�����ģ�ÿ����һ���ַ����ᴥ��һ�μ�⣬������� \d{3} �����������ǲ�������ģ�Ӧ��д�� \d{0,3}<br>
	 * ��ճ��ʱ��ճ������ֻҪ������Ҫ�󣬻�ȫ������
	 *
	 * @author ����
	 */
	public static class PattenFilter implements InputFilter {

		protected Pattern pattern;

		public PattenFilter(Pattern pattern) {
			this.pattern = pattern;
		}

		public PattenFilter(String pattern) {
			this.pattern = Pattern.compile(pattern);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			// Log.wtf("filter","source:" + source + ",start:" + start + ",end:" + end + ",dest:" + dest + ",dstart:" + dstart + ",dend:" + dend);
			// �����滻�������
			char[] s0 = dest.toString().toCharArray(); // ԭʼ��
			char[] s1 = source.toString().toCharArray(); // �滻��
			char[] s2 = new char[s0.length - dend + dstart + end - start]; // �滻��
			int i, c = 0;
			for (i = 0; i < dstart; i++)
				s2[c++] = s0[i]; // ͷ
			for (i = start; i < end; i++)
				s2[c++] = s1[i]; // �滻����
			for (i = dend; i < s0.length; i++)
				s2[c++] = s0[i]; // β
			return pattern.matcher(String.valueOf(s2)).matches() ? null : "";
		}

	}

	/**
	 * ��������������������ṩ������Ӧ����һ�����ϼ�⣬�����ճ��ʱ������ַ��жϣ�ֻ���·���Ҫ���
	 *
	 * @author ����
	 */
	public static class SetPattenFilter extends PattenFilter {

		public SetPattenFilter(Pattern pattern) {
			super(pattern);
		}

		public SetPattenFilter(String pattern) {
			super(pattern);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			if (pattern.matcher(source.subSequence(start, end)).matches())
				return null;

			if (end - start == 1) {
				return "";
			}

			SpannableStringBuilder filtered = new SpannableStringBuilder(source, start, end);
			for (int i = filtered.length(); i > 0; i--) {
				if (!pattern.matcher(filtered.subSequence(i - 1, i)).matches()) {
					filtered.delete(i - 1, i);
				}
			}
			return filtered;
		}

		/** ��Ƿ��ţ�ȫ�Ƿ��ţ����� */
		public static final String GENERAL_TEXT = "[\\x20-\\x7f\\u2000-\\u206f\\u3000-\\u303f\\u4e00-\\u9fa5\\uff00-\\uffef]*";

		/** ֧������-������ĸ��� */
		public static final String PAY_PASSWORD_PATTERN = "([a-z]|[A-Z]|[0-9])*";

	}

}
