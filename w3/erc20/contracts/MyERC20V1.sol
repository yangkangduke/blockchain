pragma solidity ^0.8.9;


import "@openzeppelin/contracts-upgradeable/token/ERC20/ERC20Upgradeable.sol";

// 0x595c128a306ad9ae830030753e8f41201c314882
contract MyERC20V1 is ERC20Upgradeable {

  function initialize() external initializer {
      __ERC20_init("yangkang", "yk");

      _mint(msg.sender, 500e18);
  }

}