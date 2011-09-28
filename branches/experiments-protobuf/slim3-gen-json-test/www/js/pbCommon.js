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
		this.mp2_4 = Math.pow(2, 4);
		this.mp2_32 = Math.pow(2, 32);
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
			hiInt = 0;
			lowInt = 0;
			while (shift < 21) {
				b = this.readRawByte();
				if(b == null) return null;
				lowInt |= (b & 0x7F) << shift;
				if ((b & 0x80) == 0) {
					return new Array(hiInt, lowInt);
				}
				shift += 7;
			}
			b = this.readRawByte();
			if(b == null) return null;
			lowInt |= (b & 0xF) << shift;
			hiInt |= (b & 0x70) >> 4;
			if ((b & 0x80) == 0) {
				return new Array(hiInt, rowInt);
			}
			shift = shift + 7 - 32;
			while (shift < 32) {
				b = this.readRawByte();
				if(b == null) return null;
				hiInt |= (b & 0x7F) << shift;
				if ((b & 0x80) == 0) {
					return new Array(hiInt, lowInt);
				}
				shift += 7;
			}
			throw Error("malformedVarint");
		};
		this.readRawLittleEndian32 = function(){
			var b1 = this.readRawByte();
			var b2 = this.readRawByte();
			var b3 = this.readRawByte();
			var b4 = this.readRawByte();
			return  (b1      ) +
					(b2 <<  8) +
					(b3 << 16) +
					((b4 << 24) >>> 0);
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
			var frac = b1 + (b2 << 8) + ((b3 & 0x7f) << 16);
			var exp = (b3 >> 7) + ((b4 & 0x7f) << 1);
			var sign = (b4 & 0x80) != 0 ? -1 : 1;
			if(frac == 0 && exp == 0) return 0;
			if(exp === 0xff){
				return frac != 0 ? NaN : (sign * Infinity);
			}
			return sign * (frac + 0x800000) * Math.pow(2, exp - 127 - 23);
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
			var frac =
				b1 + (b2 << 8) + (b3 << 16) + ((b4 << 24) >>> 0) +
				((b5 + (b6 << 8) + ((b7 & 0x0f) << 16)) * this.mp2_32);
			var exp = 
				((b7 & 0xf0) >> 4) +
				((b8 & 0x7f) << 4);
			var sign = (b8 & 0x80) != 0 ? -1 : 1;
			if(frac == 0 && exp == 0) return 0;
			if(exp === 0x7ff){
				return frac != 0 ? NaN : (sign * Infinity);
			}
			return sign * (frac + 0x10000000000000) * Math.pow(2, exp - 1023 - 52);
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

