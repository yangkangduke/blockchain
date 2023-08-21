// SPDX-License-Identifier: MIT
pragma solidity ^0.8.9;

import "@openzeppelin/contracts/interfaces/IERC20.sol";

contract Vault {

 mapping(address => uint) public deposited;
 address public immutable token;

   event Withdrawal(address indexed account, uint256 amount);

 
  constructor(address _token) {
        token = _token;
        // erc1820.setInterfaceImplementer(address(this), TOKENS_RECIPIENT_INTERFACE_HASH, address(this));
        
    }

function deposit(uint amount) public {
    IERC20(token).transferFrom(msg.sender, address(this), amount);
    deposited[msg.sender] += amount;
 }


  // 提取
  function withdraw() external {
    uint256 amount = deposited[msg.sender];
    require(amount > 0,"Insufficient balance");
    deposited[msg.sender] = 0;
    payable(msg.sender).transfer(amount);
    emit Withdrawal(msg.sender, amount);
  }
}