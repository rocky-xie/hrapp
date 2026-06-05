import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Person } from '@/shared/model/person.model';

import PersonService from './person.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Person Service', () => {
    let service: PersonService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PersonService();
      currentDate = new Date();
      elemDefault = new Person(
        123,
        'AAAAAAA',
        'AAAAAAA',
        0,
        'MALE',
        'AAAAAAA',
        'AAAAAAA',
        'NEWCOMER',
        currentDate,
        false,
        false,
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { joinDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Person', async () => {
        const returnedFromService = { id: 123, joinDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { joinDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Person', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Person', async () => {
        const returnedFromService = {
          employeeCode: 'BBBBBB',
          personName: 'BBBBBB',
          age: 1,
          gender: 'BBBBBB',
          department: 'BBBBBB',
          currentRole: 'BBBBBB',
          employmentStatus: 'BBBBBB',
          joinDate: dayjs(currentDate).format(DATE_FORMAT),
          mentorFlag: true,
          coreCandidateFlag: true,
          note: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { joinDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Person', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Person', async () => {
        const patchObject = { gender: 'BBBBBB', department: 'BBBBBB', mentorFlag: true, coreCandidateFlag: true, ...new Person() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { joinDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Person', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Person', async () => {
        const returnedFromService = {
          employeeCode: 'BBBBBB',
          personName: 'BBBBBB',
          age: 1,
          gender: 'BBBBBB',
          department: 'BBBBBB',
          currentRole: 'BBBBBB',
          employmentStatus: 'BBBBBB',
          joinDate: dayjs(currentDate).format(DATE_FORMAT),
          mentorFlag: true,
          coreCandidateFlag: true,
          note: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { joinDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Person', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Person', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Person', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
