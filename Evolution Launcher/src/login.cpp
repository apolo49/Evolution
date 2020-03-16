#include <iostream>
#include <functional>
#include <string>
#include <bitset>
#include <algorithm>
#include <vector>
#include <thread>
#include <sstream>
#include "sha3.cpp"

static bool login(char buffPass[64], char buffUsername[64]) {

	bool result = false;

	std::string key = "Evolution";
	std::string Message = buffPass;
	std::cout << Message;

	std::string hashresRet = HMACSHA512(Message, key);

	int i;

	for (i = 0; i < 32; i++) {
		hashresRet = HMACSHA512(hashresRet, key.append(std::to_string(i)));
	}

	if (result != true){
		std::cout << "Result= "+hashresRet;
	}

	return result;
}