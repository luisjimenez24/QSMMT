# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGI13 = QuantumCircuit(q0)

mutantQGI13.h(q0)
mutantQGI13.z(q0)
mutantQGI13.x(q0)

