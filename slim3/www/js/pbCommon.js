/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
var pbCommon = {
	ByteArrayInputStream_read: function(){
		if(this.index < this.limit){
			return this.bytes[this.index++] & 0xff;
		} else{
			return null;
		}
	},
	ByteArrayInputStream: function(bytes){
		this.index = 0;
		this.bytes = bytes;
		this.limit = bytes.length;
		this.limits = [];
		this.read = pbCommon.ByteArrayInputStream_read;
		this.pushLimit = function(size){
			this.limits.push(this.limit);
			this.limit = this.index + size;
		};
		this.popLimit = function(){
			if(this.limits.length == 0) return null;
			var ret = this.limit;
			this.limit = this.limits.pop();
			return ret;
		};
	},
	TextInputStream: function(text){
		index = 0;
		limit = text.length;
		limits = [];
		this.read = function(){
			if(index < limit){
				return text.charCodeAt(index++) & 0xff;
			} else{
				return null;
			}
		};
		this.pushLimit = function(size){
			limits.push(limit);
			limit = index + size;
		};
		this.popLimit = function(){
			if(limits.length == 0) return null;
			l = limit;
			limit = limits.pop();
			return l;
		};
	},
	CodedInputStream: function(is){
		this.mp_2_31 = Math.pow(2, 31);
		this.mp_2_32 = Math.pow(2, 32);
		this.mp_2_62 = Math.pow(2, 62);
		this.mp_2_m149 = Math.pow(2, -149);
		this.mp_2_m1074 = Math.pow(2, -1074);
		this.is = is;
		this.pushLimit = function(size){
			this.is.pushLimit(size);
		};
		this.popLimit = function(){
			this.is.popLimit();
		};
		this.readRawByte = function(){
			return this.is.read();
		};
		this.readRawVarint32 = function(){
			var b = this.readRawByte();
			if(b == null) return null;
			var v = b & 0x7f;
			if((b & 0x80) == 0){
				return v;
			}
			var shift = 7;
			while(shift <= 21){
				b = this.readRawByte();
				if(b == null) return null;
				v += (b & 0x7f) << shift;
				if ((b & 0x80) == 0) {
					return v;
				}
				shift += 7;
			}
			b = this.readRawByte();
			if(b == null) return null;
			v |= b << 28;
			if((b & 0x80) != 0){
				for(var i = 0; i < 5; i++){
					if((this.readRawByte() & 0x80) == 0) return v;
				}
				throw Error("malformedVarint");
			}
			return v;
		};
		this.readRawVarint64 = function(){
			var b = this.readRawByte();
			if(b == null) return null;
			var low31 = b & 0x7f;
			if((b & 0x80) == 0){
				return low31;
			}
			var shift = 7;
			while(shift <= 21){
				b = this.readRawByte();
				if(b == null) return null;
				low31 += (b & 0x7f) << shift;
				if((b & 0x80) == 0) return low31;
				shift += 7;
			}
			b = this.readRawByte();
			if(b == null) return null;
			low31 += (b & 0x07) << shift; // shift==28
			var hi31 = (b & 0x78) >> 3;
			if((b & 0x80) == 0){
				return hi31 * this.mp_2_31 + low31;
			}
			shift = 4;
			while(shift <= 18){
				b = this.readRawByte();
				if(b == null) return null;
				hi31 += (b & 0x7f) << shift;
				if((b & 0x80) == 0){
					return hi31 * this.mp_2_31 + low31;
				}
				shift += 7;
			}
			b = this.readRawByte();
			if(b == null) return null;
			hi31 += (b & 0x3f) << shift; // shift==25
			var ex = (b & 0x40) >> 6;
			if((b & 0x80) == 0){
				return ex * this.mp_2_62 + hi31 * this.mp_2_31 + low31;
			}
			b = this.readRawByte();
			if(b == null) return null;
			if((b & 0x80) != 0){
				throw Error("malformedVarint");						
			}
			// negative
			var v = ex * Math.pow(2, 62) + hi31 * Math.pow(2, 31) + low31;
			if(hi31 == 0x7fffffff && ex == 1){
				return low31 | 0x80000000;
			}
			return -4294967296 + low31 +
				(hi31 * Math.pow(2, 31) + ex * Math.pow(2, 62) -9223372032559808512);
		};
		this.readRawVarint642 = function(){
			var b = this.readRawByte();
			if(b == null) return null;
			var lowInt = b & 0x7f;
			if((b & 0x80) == 0){
				return new Array(0, lowInt);
			}
			var shift = 7;
			while(shift <= 21){
				b = this.readRawByte();
				if(b == null) return null;
				lowInt += (b & 0x7f) << shift;
				if ((b & 0x80) == 0) {
					return new Array(0, lowInt);
				}
				shift += 7;
			}
			b = this.readRawByte();
			if(b == null) return null;
			lowInt += ((b & 0xf) << 28) >>> 0;
			var hiInt = (b & 0x70) >> 4;
			if ((b & 0x80) == 0){
				return new Array(hiInt, lowInt);
			}
			shift = 3;
			while(shift <= 24){
				b = this.readRawByte();
				if(b == null) return null;
				hiInt += (b & 0x7F) << shift;
				if ((b & 0x80) == 0) {
					return new Array(hiInt, lowInt);
				}
				shift += 7;
			}
			b = this.readRawByte();
			if(b == null) return null;
			hiInt += ((b & 0x1) << shift) >>> 0;
			if ((b & 0x80) == 0) {
				return new Array(hiInt, lowInt);
			}
			throw Error("malformedVarint");
		};
		this.readRawLittleEndian32 = function(){
			var b1 = this.readRawByte();
			var b2 = this.readRawByte();
			var b3 = this.readRawByte();
			var b4 = this.readRawByte();
			return b1 + (b2 << 8) + (b3 << 16) + ((b4 << 24) >>> 0);
		};
		this.readRawLittleEndian64 = function(){
			var b1 = this.readRawByte();
			var b2 = this.readRawByte();
			var b3 = this.readRawByte();
			var b4 = this.readRawByte();
			var b5 = this.readRawByte();
			var b6 = this.readRawByte();
			var b7 = this.readRawByte();
			var b8 = this.readRawByte();
			return new Array(
					b5 + (b6 << 8) + (b7 << 16) + ((b8 << 24) >>> 0)
					, b1 + (b2 << 8) + (b3 << 16) + ((b4 << 24) >>> 0)
					);
		};
		this.readTag = function(){
			return this.readRawVarint32();
		};
		this.readFieldNum = function(){
			var t = this.readTag();
			if(t != null) return t >> 3;
			return t;
		};
		this.readInt32 = function(){
			return this.readRawVarint32();
		};
		this.readInt64 = function(){
			return this.readRawVarint64();
		};
		this.readFloat = function(){
			var b1 = this.readRawByte();
			var b2 = this.readRawByte();
			var b3 = this.readRawByte();
			var b4 = this.readRawByte();
			if(b1 == null || b2 == null || b3 == null || b4 == null) return null;
			var frac = b1 + (b2 << 8) + ((b3 & 0x7f) << 16);
			var exp = (b3 >> 7) + ((b4 & 0x7f) << 1);
			var sign = (b4 & 0x80) != 0 ? -1 : 1;
			if(exp == 0){
				if(frac == 0) return (sign == -1) ? -0 : 0;
				return sign * frac * this.mp_2_m149;
			}
			if(exp === 0xff){
				return frac != 0 ? NaN : (sign * Infinity);
			}
			return sign * (frac + 0x800000) * Math.pow(2, exp - 150);
		};
		this.readDouble = function(){
			var b1 = this.readRawByte();
			var b2 = this.readRawByte();
			var b3 = this.readRawByte();
			var b4 = this.readRawByte();
			var b5 = this.readRawByte();
			var b6 = this.readRawByte();
			var b7 = this.readRawByte();
			var b8 = this.readRawByte();
			if(b1 == null || b2 == null || b3 == null || b4 == null
					|| b5 == null || b6 == null || b7 == null || b8 == null) return null;
			var frac = b1 + (b2 << 8) + (b3 << 16) + ((b4 << 24) >>> 0) +
				((b5 + (b6 << 8) + ((b7 & 0x0f) << 16)) * this.mp_2_32);
			var exp = ((b7 & 0xf0) >> 4) + ((b8 & 0x7f) << 4);
			var sign = (b8 & 0x80) != 0 ? -1 : 1;
			if(exp == 0){
				if(frac == 0) return (sign == 1) ? 0 : -0;
				return sign * frac * this.mp_2_m1074;
			}
			if(exp === 0x7ff){
				return frac != 0 ? NaN : (sign * Infinity);
			}

			return sign * (frac + 0x10000000000000) * Math.pow(2, exp - 1075);
		};
		this.readBool = function(){
			r = this.readRawVarint32();
			if(r == null) return null;
			return r != 0;
		};
		this.readString = function(){
			// 
			var size = this.readRawVarint32();
			if(size == null) return;
			var str = "";
			for(i = 0; i < size; i++){
				c = this.readRawByte();
				if(c == null) break;
				if(c <= 0x7f) {
					str += String.fromCharCode(c);
				} else if((0x60 <= c) && (c < 224)) {
					c2 = this.readRawByte();
					size--;
					if(c2 == null) break;
					str += String.fromCharCode(
							((c & 0x1f) << 6) | (c2 & 63)
							);
				} else {
					c2 = this.readRawByte();
					size--;
					if(c == null) break;
					c3 = this.readRawByte();
					size--;
					if(c == null) break;
					str += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
	 			}
			}
			return str;
		};
	},
	doReadModel: function(cin, def, factory){
		var m = factory();
		while(true){
			var n = cin.readFieldNum();
			if(n == null) break;
			var d = def[n];
			if(d != null) def[n](cin, m);
		}
		return m;
	},
	readModel: function(text, def, factory){
		var cin = new this.CodedInputStream(new this.TextInputStream(text));
		return this.doReadModel(cin, def, factory);
	},
	readModels: function(text, def, factory){
		var cin = new this.CodedInputStream(new this.TextInputStream(text));
		var models = [];
		while((size = cin.readRawVarint32()) != null){
			cin.pushLimit(size);
			var m = this.doReadModel(cin, def, factory);
			cin.popLimit();
			if(m != null){
				models.push(m);
			}
		}
		return models;
	}
};
