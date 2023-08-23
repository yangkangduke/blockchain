const hre = require("hardhat");
async function main() {
  // 获得将要部署的合约
  const MyERC20V1 = await hre.ethers.getContractFactory("MyERC20V1");
  const greeter = await hre.upgrades.deployProxy(MyERC20V1);
  await greeter.waitForDeployment();
  console.log("Greeter deployed to:", await greeter.getAddress);
}
// goerli网络合约地址：
main()
  .then(() => process.exit(0))
  .catch(error => {
    console.error(error);
    process.exit(1);
  });