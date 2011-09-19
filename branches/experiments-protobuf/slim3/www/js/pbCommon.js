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
		this.is = is;
		this.pushLimit = function(size){
			this.is.pushLimit(size);
		}
		this.popLimit = function(){
			this.is.popLimit();
		}
		this.readRawByte = function(){
			return this.is.read();
		}
		this.readRawVarint32 = function(){
			tmp = this.readRawByte();
			if(tmp == null) return null;
			if(tmp >= 0) return tmp;
			result = tmp & 0x7f;
			if((tmp = this.readRawByte()) >= 0) {
				result |= tmp << 7;
			} else {
				result |= (tmp & 0x7f) << 7;
				if ((tmp = this.readRawByte()) >= 0) {
					result |= tmp << 14;
				} else{
					result |= (tmp & 0x7f) << 14;
					if ((tmp = this.readRawByte()) >= 0) {
						result |= tmp << 21;
					} else {
						result |= (tmp & 0x7f) << 21;
						result |= (tmp = this.readRawByte()) << 28;
						if (tmp < 0) {
							// Discard upper 32 bits.
							for ( i = 0; i < 5; i++) {
								if (this.readRawByte() >= 0) {
									return result;
								}
							}
							throw Error("malformedVarint");
						}
					}
				}
			}
			return result;
		};
		this.readRawVarint64 = function(){
			shift = 0;
			result = 0;
			while (shift < 64) {
				b = this.readRawByte();
				if(b == null) return null;
				result |= (b & 0x7F) << shift;
				if ((b & 0x80) == 0) {
					return result;
				}
				shift += 7;
			}
			throw Error("malformedVarint");
		};
		this.readRawLittleEndian32 = function(){
			b1 = this.readRawByte();
			b2 = this.readRawByte();
			b3 = this.readRawByte();
			b4 = this.readRawByte();
			return  (b1      ) |
					(b2 <<  8) |
					(b3 << 16) |
					(b4 << 24);
		};
		this.readRawLittleEndian64 = function(){
			b1 = this.readRawByte();
			b2 = this.readRawByte();
			b3 = this.readRawByte();
			b4 = this.readRawByte();
			b5 = this.readRawByte();
			b6 = this.readRawByte();
			b7 = this.readRawByte();
			b8 = this.readRawByte();
			return  (b1      ) +
					(b2 <<  8) +
					(b3 << 16) +
					(b4 << 24) +
					(b5 * Math.pow(2, 32)) +
					(b6 * Math.pow(2, 40)) +
					(b7 * Math.pow(2, 48)) +
					(b8 * Math.pow(2, 56));
		};
		this.readTag = function(){
			return this.readRawVarint32();
		};
		this.readFieldNum = function(){
			t = this.readTag();
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
			rv = this.readRawLittleEndian32();
			sign = rv & 0x80000000;
			exp  = (rv >> 23) & 0xff;
			frac = rv & 0x7fffff;
			if(!rv || rv === 0x80000000){ // 0.0 or -0.0
				return 0;
			}
			if(exp === 0xff){ // NaN or Infinity
				return frac ? NaN : Infinity;
			}
			return (sign ? -1 : 1) * (frac | 0x800000) * Math.pow(2, exp - 127 - 23);
		};
		this.readDouble = function(){
			rv = this.readRawLittleEndian64();
			sign = rv & 0x8000000000000000;
			exp  = (rv / Math.pow(2, 52)) & 0x7ff;
			frac = rv & 0xfffffffffffff;
			if(!rv || rv === 0x8000000000000000){ // 0.0 or -0.0
				return 0;
			}
			if(exp === 0x7ff){ // NaN or Infinity
				return frac ? NaN : Infinity;
			}
			return (sign ? -1 : 1) * (frac + 0x10000000000000) * Math.pow(2, exp - 1023 - 52);
		};
		this.readBool = function(){
			r = this.readRawVarint32();
			if(r == null) return null;
			return r != 0;
		};
		this.readString = function(){
			size = this.readRawVarint32();
			if(size == null) return;
			bytes = [];
			for(i = 0; i < size; i++){
				v = this.readRawByte();
				if(v == null){
					break;
				}
				bytes.push(v);
			}
			return String.fromCharCode.apply(String, bytes);
		};
	},
	doReadModel: function(cin, def){
		ret = {};
		while(true){
			n = cin.readFieldNum();
			if(n == null) break;
			d = def[n];
			if(d != null) def[n](cin, ret);
		}
		return ret;
	},
	readModel: function(text, def){
		cin = new this.CodedInputStream(new this.TextInputStream(text));
		return this.doReadModel(cin, def);
	},
	readModels: function(text, def){
		cin = new this.CodedInputStream(new this.TextInputStream(text));
		models = [];
		while((size = cin.readRawVarint32()) != null){
			cin.pushLimit(size);
			m = this.doReadModel(cin, def);
			cin.popLimit();
			if(m != null){
				models.push(m);
			}
		}
		return models;
	}
};

