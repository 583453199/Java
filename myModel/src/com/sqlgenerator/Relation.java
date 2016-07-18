package com.sqlgenerator;


public enum Relation {
	/**
	 * =
	 * 
	 */
	EQUALS {
		public String toString() {
			return "=";
		}
	},
	/**
	 * !=
	 * 
	 */
	NOT_EQUALS {
		public String toString() {
			return "!=";
		}
	},
	/**
	 * >
	 * 
	 */
	GREATER_THAN {
		public String toString() {
			return ">";
		}
	},
	/**
	 * >=
	 * 
	 */
	GREATER_THAN_EQUALS {
		public String toString() {
			return ">=";
		}
	},

	/**
	 * <
	 * 
	 */
	LESS_THAN {
		public String toString() {
			return "<";
		}
	},
	/**
	 * <=
	 * 
	 */
	LESS_THAN_EQUALS {
		public String toString() {
			return "<=";
		}
	}
}
