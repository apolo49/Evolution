#include <iostream>
#include <functional>
#include <string>
#include <sstream>
#include <jdbc.h>
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


        try {
            sql::Driver* driver;
            sql::Connection* con;
            sql::Statement* stmt;
            sql::ResultSet* res;

            /* Create a connection */
            driver = get_driver_instance();
            con = driver->connect("tcp://127.0.0.1:3306", "root", "root");
            /* Connect to the MySQL test database */
            con->setSchema("test");

            stmt = con->createStatement();
            res = stmt->executeQuery("SELECT 'Hello World!' AS _message");
            while (res->next()) {
                std::cout << "\t... MySQL replies: ";
                /* Access column data by alias or column name */
                std::cout << res->getString("_message") << std::endl;
                std::cout << "\t... MySQL says it again: ";
                /* Access column data by numeric offset, 1 is the first column */
                std::cout << res->getString(1) << std::endl;
            }
            delete res;
            delete stmt;
            delete con;

        }
        catch (sql::SQLException & e) {
            std::cout << "# ERR: SQLException in " << __FILE__;
            std::cout << "(" << __FUNCTION__ << ") on line " << __LINE__ << std::endl;
            std::cout << "# ERR: " << e.what();
            std::cout << " (MySQL error code: " << e.getErrorCode();
            std::cout << ", SQLState: " << e.getSQLState() << " )" << std::endl;
        }
	}

	return result;
}