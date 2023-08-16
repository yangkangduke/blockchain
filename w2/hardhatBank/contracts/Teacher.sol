// SPDX-License-Identifier: MIT
pragma solidity ^0.8.9;



// 分数合约
contract Score {
    mapping(address => uint) public  scores;
    address public owner;
    address public teacher;

    constructor() {
        owner = msg.sender;
    }

    modifier onlyOwner() {
        require(msg.sender == owner, "Not owner");
        _;
    }
     modifier onlyTeacher() {
        require(msg.sender == teacher, "Not teacher");
        _;
    }

    function setTeacher(address _teacher) external onlyOwner {
        teacher = _teacher;
    }

    function setScore(address _student,uint _score) external onlyTeacher {
        require(_score <= 100, "not 100");
        // 设置
        scores[_student] = _score;
    }
    
    
}

// 接口合约
interface IScore {
    function setScore(address _student, uint _score) external;
}

// test
contract Teacher {
IScore score;
 constructor(address s) {
    score = IScore(s);
  }
    function teacherSet(address _student, uint _score) external {
        score.setScore(_student, _score);
    }
}