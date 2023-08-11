require("@nomicfoundation/hardhat-toolbox");
require("@nomiclabs/hardhat-etherscan");

/** @type import('hardhat/config').HardhatUserConfig */
module.exports = {
  solidity: "0.8.19",
  etherscan: {
    // Your API key for Etherscan
    // Obtain one at https://etherscan.io/
    apiKey: "K6WH9PP9MCWHTEQ7P3XXB7C2EFCNNSEJY3"
  },
  networks: {
    goerli: {
      url: "https://eth-goerli.g.alchemy.com/v2/TEeAfIGvdAudHKz9qRZ2ctuNOpozkoWE", // Infura 节点 URL
      accounts: ["0x68abfbb7ec7b615605d904e94bd1ac5b23393584ffc751c4937ab5a923ee398a"], // 要用于部署合约的私钥
    },
  },


};
