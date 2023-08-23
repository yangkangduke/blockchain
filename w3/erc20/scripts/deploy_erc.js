const { ethers, upgrades } = require("hardhat");

async function main() {
  // Deploying
  const MyERC20V1 = await ethers.getContractFactory("MyERC20V1");
  const instance = await upgrades.deployProxy(MyERC20V1);
  await instance.waitForDeployment();

  console.log("v1",instance);

  // Upgrading
  const MyERC20V2 = await ethers.getContractFactory("MyERC20V2");
  const upgraded = await upgrades.upgradeProxy(await instance.getAddress(), MyERC20V2);
  console.log("v2",upgraded);
}

main();