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
	ByteArrayInputStream: function(bytes){
		this.index = 0;
		this.bytes = bytes;
		this.limit = bytes.length;
		this.limits = [];
	},
	TextInputStream: function(text){
		this.text = text;
		this.index = 0;
		this.limit = text.length;
		this.limits = [];
	},
	CodedInputStream: function(is){
		this.is = is;
	},
	doReadModel: function(cin, def, factory, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 0;
		if(typeof(curDepth) == "undefined") curDepth = 0;
		var m = factory();
		while(true){
			var n = cin.readFieldNum();
			if(n == null) break;
			var d = def[n];
			if(d != null) def[n](cin, m, maxDepth, curDepth);
		}
		return m;
	},
	readModel: function(text, def, factory, maxDepth, curDepth){
		var cin = text;
		if(typeof(text) == "string"){
			cin = new this.CodedInputStream(new this.TextInputStream(text));
		}
		return this.doReadModel(cin, def, factory, maxDepth, curDepth);
	},
	readModels: function(text, def, factory, maxDepth, curDepth){
		var cin = new this.CodedInputStream(new this.TextInputStream(text));
		var models = [];
		while((size = cin.readRawVarint32()) != null){
			cin.pushLimit(size);
			var m = this.doReadModel(cin, def, factory, maxDepth, curDepth);
			cin.popLimit();
			if(m != null){
				models.push(m);
			}
		}
		return models;
	}
};
pbCommon.ByteArrayInputStream.read = function(){
	if(this.index < this.limit){
		return this.bytes[this.index++] & 0xff;
	} else{
		return null;
	}
};
pbCommon.ByteArrayInputStream.pushLimit = function(size){
	this.limits.push(this.limit);
	this.limit = this.index + size;
};
pbCommon.ByteArrayInputStream.popLimit = function(){
	if(this.limits.length == 0) return null;
	var ret = this.limit;
	this.limit = this.limits.pop();
	return ret;
};
pbCommon.TextInputStream.prototype.read = function(){
	if(this.index < this.limit){
		return this.text.charCodeAt(this.index++) & 0xff;
	} else{
		return null;
	}
};
pbCommon.TextInputStream.prototype.pushLimit = function(size){
	this.limits.push(this.limit);
	this.limit = this.index + size;
};
pbCommon.TextInputStream.prototype.popLimit = function(){
	if(this.limits.length == 0) return null;
	var l = this.limit;
	this.limit = this.limits.pop();
	return l;
};
pbCommon.CodedInputStream.prototype.mp_2_28 = Math.pow(2, 28);
pbCommon.CodedInputStream.prototype.mp_2_32 = Math.pow(2, 32);
pbCommon.CodedInputStream.prototype.mp_2_m149 = Math.pow(2, -149);
pbCommon.CodedInputStream.prototype.mp_2_m1074 = Math.pow(2, -1074);
pbCommon.CodedInputStream.prototype.pushLimit = function(size){
	this.is.pushLimit(size);
};
pbCommon.CodedInputStream.prototype.popLimit = function(){
	this.is.popLimit();
};
pbCommon.CodedInputStream.prototype.readRawByte = function(){
	return this.is.read();
};
pbCommon.CodedInputStream.prototype.readRawVarint32 = function(){
	var b = this.readRawByte();
	if(b == null) return null;
	var v = b & 0x7f;
	if((b & 0x80) == 0){
		return v;
	}
	var shift = 7;
	while(shift <= 21){
		b = this.readRawByte();
		v += (b & 0x7f) << shift;
		if ((b & 0x80) == 0) {
			return v;
		}
		shift += 7;
	}
	b = this.readRawByte();
	v |= b << 28;
	if((b & 0x80) != 0){
		for(var i = 0; i < 5; i++){
			if((this.readRawByte() & 0x80) == 0) return v;
		}
		throw Error("malformedVarint");
	}
	return v;
};
pbCommon.CodedInputStream.prototype.readRawVarint64 = function(){
	var b = this.readRawByte();
	if(b == null) return null;
	var low32 = b & 0x7f;
	if((b & 0x80) == 0){
		return {
			value: low32,
			hi32: 0,
			low32: low32
		};
	}
	var shift = 7;
	while(shift <= 21){
		b = this.readRawByte();
		low32 += (b & 0x7f) << shift;
		if((b & 0x80) == 0){
			return {
				value: low32,
				hi32: 0,
				low32: low32
			};
		}
		shift += 7;
	}
	b = this.readRawByte();
	low32 += (b & 0x0f) * this.mp_2_28; // shift==28
	var hi31 = (b & 0x70) >> 4;
	if((b & 0x80) == 0){
		hi31 *= this.mp_2_32;
		return {
			value: hi31 + low32,
			hi32: hi31,
			low32: low32
		};
	}
	shift = 3;
	while(shift <= 24){
		b = this.readRawByte();
		hi31 += (b & 0x7f) << shift;
		if((b & 0x80) == 0){
			hi31 *= this.mp_2_32;
			return {
				value: hi31 + low32,
				hi32: hi31,
				low32: low32
			};
		}
		shift += 7;
	}
	b = this.readRawByte();
	if((b & 0x80) != 0){
		throw Error("malformedVarint");						
	}
	// negative
	hi31 = (hi31 | 0x80000000) * this.mp_2_32;
	return {
		value: hi31 + low32,
		hi32: hi31,
		low32: low32
	};
};
pbCommon.CodedInputStream.prototype.readRawLittleEndian32 = function(){
	var b1 = this.readRawByte();
	if(b1 == null) return null;
	var b2 = this.readRawByte();
	var b3 = this.readRawByte();
	var b4 = this.readRawByte();
	return b1 + (b2 << 8) + (b3 << 16) + ((b4 << 24) >>> 0);
};
pbCommon.CodedInputStream.prototype.readRawLittleEndian64 = function(){
	var b1 = this.readRawByte();
	if(b1 == null) return null;
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
pbCommon.CodedInputStream.prototype.readTag = function(){
	return this.readRawVarint32();
};
pbCommon.CodedInputStream.prototype.readFieldNum = function(){
	var t = this.readTag();
	if(t == null) return null;
	return t >> 3;
};
pbCommon.CodedInputStream.prototype.readInt32 = function(){
	return this.readRawVarint32();
};
pbCommon.CodedInputStream.prototype.readInt64 = function(){
	return this.readRawVarint64();
};
pbCommon.CodedInputStream.prototype.readFloat = function(){
	var b1 = this.readRawByte();
	if(b1 == null) return null;
	var b2 = this.readRawByte();
	var b3 = this.readRawByte();
	var b4 = this.readRawByte();
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
pbCommon.CodedInputStream.prototype.readDouble = function(){
	var b1 = this.readRawByte();
	if(b1 == null) return null;
	var b2 = this.readRawByte();
	var b3 = this.readRawByte();
	var b4 = this.readRawByte();
	var b5 = this.readRawByte();
	var b6 = this.readRawByte();
	var b7 = this.readRawByte();
	var b8 = this.readRawByte();
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
pbCommon.CodedInputStream.prototype.readBool = function(){
	var v = this.readRawVarint32();
	if(v == null) return null;
	return v != 0;
};
pbCommon.CodedInputStream.prototype.readString = function(){
	var size = this.readRawVarint32();
	if(size == null) return null;
	var str = "";
	for(i = 0; i < size; i++){
		var c = this.readRawByte();
		if(c <= 0x7f) {
			str += String.fromCharCode(c);
		} else if((c & 0xe0) == 0xc0){
			if((i + 1) == size) break;
			var c2 = this.readRawByte();
			size--;
			str += String.fromCharCode(
					((c & 0x1f) << 6) + (c2 & 0x3f)
					);
		} else if((c & 0xf0) == 0xe0){
			if((i + 1) == size) break;
			var c2 = this.readRawByte();
			size--;
			if((i + 1) == size) break;
			var c3 = this.readRawByte();
			size--;
			str += String.fromCharCode(
					((c & 0x0f) << 12) + ((c2 & 0x3f) << 6) + (c3 & 0x3f)
					);
		} else if((c & 0xf8) == 0xf0){
			// surrogate
			if((i + 1) == size) break;
			var c2 = this.readRawByte();
			size--;
			if((i + 1) == size) break;
			var c3 = this.readRawByte();
			size--;
			if((i + 1) == size) break;
			var c4 = this.readRawByte();
			size--;
			var code = ((c & 0x3) << 18) + ((c2 & 0x3f) << 12) +
				((c3 & 0x3f) << 6) + (c4 & 0x3f) - 0x10000;
			str += String.fromCharCode((code / 0x400) + 0xD800) +
				String.fromCharCode((code % 0x400) + 0xDC00);
		}
	}
	return str;
};
