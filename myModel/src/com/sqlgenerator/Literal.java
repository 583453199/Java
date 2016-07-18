package com.sqlgenerator;


public enum Literal {
	/**
	 * is null
	 */
	IS_NULL {
		public String toString() {
			return "is null";
		}
	},

	/**
	 * is not null
	 */
	IS_NOT_NULL {
		public String toString() {
			return "is not null";
		}
	},
	/**
	 * (
	 */
	LEFT_BRACKET {
		public String toString() {
			return "(";
		}
	},
	/**
	 * ((
	 */
	LEFT_BRACKET_TWO {
		public String toString() {
			return "((";
		}
	},
	/**
	 * (((
	 */
	LEFT_BRACKET_THREE {
		public String toString() {
			return "(((";
		}
	},
	/**
	 * )
	 */

	RIGHT_BRACKET {
		public String toString() {
			return ")";
		}
	},
	/**
	 * ))
	 */

	RIGHT_BRACKET_TWO {
		public String toString() {
			return "))";
		}
	},
	/**
	 * )))
	 */
	RIGHT_BRACKET_THREE {
		public String toString() {
			return ")))";
		}

	},
	/**
	 * and
	 */
	AND {
		public String toString() {
			return "and";
		}
	},
	/**
	 * and (
	 */
	AND_LEFT_BRACKET {
		public String toString() {
			return "and (";
		}
	},
	/**
	 * and ((
	 */
	AND_LEFT_BRACKET_TWO {
		public String toString() {
			return "and ((";
		}
	},
	/**
	 * or
	 */
	OR {
		public String toString() {
			return "or";
		}
	},
	/**
	 * or (
	 */
	OR_LEFT_BRACKET {
		public String toString() {
			return "or (";
		}
	},
	/**
	 * or ((
	 */
	OR_LEFT_BRACKET_TWO {
		public String toString() {
			return "or ((";
		}
	},
	/**
	 * not
	 */
	NOT {
		public String toString() {
			return "not";
		}
	}
}
