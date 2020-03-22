#include <iostream>
#include <functional>
#include <string>
#include <bitset>
#include <algorithm>
#include <vector>
#include <sstream>
#include <Bits.h>
#include <unordered_map>
#include <inttypes.h>


inline uint64_t Reverse32(uint64_t value)
{
    return (value & 0x00000000000000FF) << 56 | (value & 0x000000000000FF00) << 40 |
        (value & 0x0000000000FF0000) << 24 | (value & 0x00000000FF000000) << 8 |
        (value & 0x000000FF00000000) >> 8 | (value & 0x0000FF0000000000) >> 24 |
        (value & 0x00FF000000000000) >> 40 | (value & 0xFF00000000000000) >> 56;
}

static uint64_t rightRotate(uint64_t n, uint64_t d)
{
    /* In n>>d, first d bits are 0.
    To put last 3 bits of at
    first, do bitwise or of n>>d
    with n <<(INT_BITS - d) */
    return (n >> d) | (n << (32 - d));
}

inline uint64_t BigEndian64(uint64_t value)
{
#if __BYTE_ORDER__ == __ORDER_LITTLE_ENDIAN__
    return Reverse32(value);
#elif __BYTE_ORDER__ == __ORDER_BIG_ENDIAN__
    return value;
#else
#    error unsupported endianness
#endif
}

static void createMap(std::unordered_map<std::string, char>* um) {

    (*um)["0000"] = '0';
    (*um)["0001"] = '1';
    (*um)["0010"] = '2';
    (*um)["0011"] = '3';
    (*um)["0100"] = '4';
    (*um)["0101"] = '5';
    (*um)["0110"] = '6';
    (*um)["0111"] = '7';
    (*um)["1000"] = '8';
    (*um)["1001"] = '9';
    (*um)["1010"] = 'a';
    (*um)["1011"] = 'b';
    (*um)["1100"] = 'c';
    (*um)["1101"] = 'd';
    (*um)["1110"] = 'e';
    (*um)["1111"] = 'f';

}

static std::string convertBinToHex(std::string bin)
{
    int l = bin.size();
    int t = bin.find_first_of('.');

    // length of string before '.' 
    int len_left = t != -1 ? t : l;

    // add min 0's in the beginning to make 
    // left substring length divisible by 4  
    for (int i = 1; i <= (4 - len_left % 4) % 4; i++)
        bin = '0' + bin;

    // if decimal point exists     
    if (t != -1)
    {
        // length of string after '.' 
        int len_right = l - len_left - 1;

        // add min 0's in the end to make right 
        // substring length divisible by 4  
        for (int i = 1; i <= (4 - len_right % 4) % 4; i++)
            bin = bin + '0';
    }

    // create map between binary and its 
    // equivalent hex code 
    std::unordered_map<std::string, char> bin_hex_map;
    createMap(&bin_hex_map);

    int i = 0;
    std::string hex = "";

    while (1)
    {
        // one by one extract from left, substring 
        // of size 4 and add its hex code 
        hex += bin_hex_map[bin.substr(i, 4)];
        i += 4;
        if (i == bin.size())
            break;

        // if '.' is encountered add it 
        // to result 
        if (bin.at(i) == '.')
        {
            hex += '.';
            i++;
        }
    }

    // required hexadecimal number 
    return hex;
}

static uint64_t mod64add(uint64_t operand1, uint64_t operand2) {

    uint64_t result = operand1 + operand2;
    if (result > std::pow(2, 64)) {
        result -= pow(2, 64);
    }
    return result;
}

static std::string SHA512 (std::string key) {

    //Programmed by Joe Targett from Wikipedia.org psuedocode algorithm in SHA2 page.

     std::uint64_t h0 = 0x6a09e667f3bcc908;
     std::uint64_t h1 = 0xbb67ae8584caa73b;
     std::uint64_t h2 = 0x3c6ef372fe94f82b;
     std::uint64_t h3 = 0xa54ff53a5f1d36f1;
     std::uint64_t h4 = 0x510e527fade682d1;
     std::uint64_t h5 = 0x9b05688c2b3e6c1f;
     std::uint64_t h6 = 0x1f83d9abfb41bd6b;
     std::uint64_t h7 = 0x5be0cd19137e2179;

     std::uint64_t k[80] = { 0x428a2f98d728ae22, 0x7137449123ef65cd, 0xb5c0fbcfec4d3b2f, 0xe9b5dba58189dbbc, 0x3956c25bf348b538,
                  0x59f111f1b605d019, 0x923f82a4af194f9b, 0xab1c5ed5da6d8118, 0xd807aa98a3030242, 0x12835b0145706fbe,
                  0x243185be4ee4b28c, 0x550c7dc3d5ffb4e2, 0x72be5d74f27b896f, 0x80deb1fe3b1696b1, 0x9bdc06a725c71235,
                  0xc19bf174cf692694, 0xe49b69c19ef14ad2, 0xefbe4786384f25e3, 0x0fc19dc68b8cd5b5, 0x240ca1cc77ac9c65,
                  0x2de92c6f592b0275, 0x4a7484aa6ea6e483, 0x5cb0a9dcbd41fbd4, 0x76f988da831153b5, 0x983e5152ee66dfab,
                  0xa831c66d2db43210, 0xb00327c898fb213f, 0xbf597fc7beef0ee4, 0xc6e00bf33da88fc2, 0xd5a79147930aa725,
                  0x06ca6351e003826f, 0x142929670a0e6e70, 0x27b70a8546d22ffc, 0x2e1b21385c26c926, 0x4d2c6dfc5ac42aed,
                  0x53380d139d95b3df, 0x650a73548baf63de, 0x766a0abb3c77b2a8, 0x81c2c92e47edaee6, 0x92722c851482353b,
                  0xa2bfe8a14cf10364, 0xa81a664bbc423001, 0xc24b8b70d0f89791, 0xc76c51a30654be30, 0xd192e819d6ef5218,
                  0xd69906245565a910, 0xf40e35855771202a, 0x106aa07032bbd1b8, 0x19a4c116b8d2d0c8, 0x1e376c085141ab53,
                  0x2748774cdf8eeb99, 0x34b0bcb5e19b48a8, 0x391c0cb3c5c95a63, 0x4ed8aa4ae3418acb, 0x5b9cca4f7763e373,
                  0x682e6ff3d6b2b8a3, 0x748f82ee5defb2fc, 0x78a5636f43172f60, 0x84c87814a1f0ab72, 0x8cc702081a6439ec,
                  0x90befffa23631e28, 0xa4506cebde82bde9, 0xbef9a3f7b2c67915, 0xc67178f2e372532b, 0xca273eceea26619c,
                  0xd186b8c721c0c207, 0xeada7dd6cde0eb1e, 0xf57d4f7fee6ed178, 0x06f067aa72176fba, 0x0a637dc5a2c898a6,
                  0x113f9804bef90dae, 0x1b710b35131c471b, 0x28db77f523047d84, 0x32caab7b40c72493, 0x3c9ebe0a15c9bebc,
                  0x431d67c49c100d4c, 0x4cc5d4becb3e42b6, 0x597f299cfc657e2a, 0x5fcb6fab3ad6faec, 0x6c44198c4a475817 };

    
    std::string keyString(key);
    std::string converted;


    for (std::size_t i = 0; i < keyString.size(); ++i)
    {
        converted += std::bitset<8>(keyString.c_str()[i]).to_string();
    }

    std::uint64_t L = converted.length();

    converted += "1";

    uint64_t K = 0;

    while (((converted.length() + 64) % 1024) != 0) {
        converted += "0";
    }

    std::uint64_t LBigEndian = BigEndian64(L);

    std::string LBigEndianString = std::bitset<64>(LBigEndian).to_string();
    converted.append(LBigEndianString);

    long i = 0;

    converted = convertBinToHex(converted);

    while (converted[i])
        i++;

    long sizeOfArray = (i / long(256));

    std::vector<std::string> chunks(sizeOfArray);

    long j = 0;
    long str_size = i;

    i = 0;

    for (i = 0; i < str_size; i++)
    {
        std::string temp;
        temp += converted[i];
        chunks[j] += temp;
        if (i != 0) {
            if (i % 256 == 0)
                j++;
        }
    }

    i = 0;

    for (i = 0; i < chunks.size(); i++) {

        std::uint64_t w[80] = {
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000,
            0x0000000000000000, 0x0000000000000000, 0x0000000000000000, 0x0000000000000000
        };

        int l = 0;

        std::string ChunkToAdd;
        std::string currentChunk;

        for (j = 0; j < 16; j++) {

            currentChunk = chunks[i];
            int current_iteration = l;
            ChunkToAdd = "";

            for (l = current_iteration; l < (current_iteration + 8); l++) {
                if (l < currentChunk.length()) {
                    ChunkToAdd += currentChunk[l];
                }
            }

            std::istringstream reader(ChunkToAdd);
            reader >> std::hex >> w[j];
        }

        uint64_t s0;
        uint64_t s1;

        for (j = 16; j < 80; j++) {
            s0 = rightRotate(w[j - 15], 1) ^ rightRotate(w[j - 15], 8) ^ (w[j - 15] >> 7);
            s1 = rightRotate(w[j - 2], 19) ^ rightRotate(w[j - 2], 61) ^ (w[j - 2] >> 6);
            w[j] = mod64add(mod64add(mod64add(w[j - 16], s0), w[j - 7]), s1);
        }

        uint64_t a = h0;
        uint64_t b = h1;
        uint64_t c = h2;
        uint64_t d = h3;
        uint64_t e = h4;
        uint64_t f = h5;
        uint64_t g = h6;
        uint64_t h = h7;

        uint64_t S0;
        uint64_t S1;
        uint64_t ch;
        uint64_t temp1;
        uint64_t maj;
        uint64_t temp2;

        for (j = 0; j < 80; j++) {
            S1 = rightRotate(e, 14) ^ rightRotate(e, 18) ^ _rotr(e, 41);
            ch = (e & f) ^ ((~ e) & g);
            temp1 = mod64add(mod64add(mod64add(mod64add(h, S1), ch), k[j]), w[j]);

            S0 = rightRotate(a , 28) ^ rightRotate(a , 34) ^ _rotr(a , 39);
            maj = (a & b) ^ (a & c) ^ (b & c);
            temp2 = mod64add(S0, maj);

            h = g;
            g = f;
            f = e;
            e = mod64add(d, temp1);
            d = c;
            c = b;
            b = a;
            a = mod64add(temp1, temp2);
        }

        h0 = mod64add(h0, a);
        h1 = mod64add(h1, b);
        h2 = mod64add(h2, c);
        h3 = mod64add(h3, d);
        h4 = mod64add(h4, e);
        h5 = mod64add(h5, f);
        h6 = mod64add(h6, g);
        h7 = mod64add(h7, h);

    }

    return convertBinToHex(std::bitset<64>(h0).to_string()
        .append(std::bitset<64>(h1).to_string()
            .append(std::bitset<64>(h2).to_string()
                .append(std::bitset<64>(h3).to_string()
                    .append(std::bitset<64>(h4).to_string()
                        .append(std::bitset<64>(h5).to_string()
                            .append(std::bitset<64>(h6).to_string()
                                .append(std::bitset<64>(h7).to_string()))))))));
}



static std::string HMACSHA512(std::string Message, std::string Key) {

    std::string key = Key;
    std::string message = Message;

    const unsigned int BLOCKSIZE = (1024 / 8);

    if (key.length() > BLOCKSIZE) {
        key = SHA512(key);
    }
    while (key.length() < BLOCKSIZE) {
        key = key + (char)0x00;
    }

    std::string o_key_pad = key;
    for (unsigned int i = 0; i < BLOCKSIZE; i++) {
        o_key_pad[i] = key[i] ^ (char)0x5c;
    }

    std::string i_key_pad = key;
    for (unsigned int i = 0; i < BLOCKSIZE; i++) {
        i_key_pad[i] = key[i] ^ (char)0x36;
    }

    std::string set = SHA512(o_key_pad + SHA512(i_key_pad + message));

    return set;
}
