# -*- coding: utf-8 -*-

from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from numpy import pi

q0 = QuantumRegister(1, 'q0')


mutantQGR1 = QuantumCircuit(q0)

mutantQGR1.t(q0)
mutantQGR1.z(q0)

