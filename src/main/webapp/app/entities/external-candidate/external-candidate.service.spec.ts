import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { ExternalCandidate } from '@/shared/model/external-candidate.model';

import ExternalCandidateService from './external-candidate.service';

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
  describe('ExternalCandidate Service', () => {
    let service: ExternalCandidateService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ExternalCandidateService();
      currentDate = new Date();
      elemDefault = new ExternalCandidate(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        currentDate,
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          birthDate: dayjs(currentDate).format(DATE_FORMAT),
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
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

      it('should create a ExternalCandidate', async () => {
        const returnedFromService = {
          id: 123,
          birthDate: dayjs(currentDate).format(DATE_FORMAT),
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { birthDate: currentDate, createdAt: currentDate, updatedAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a ExternalCandidate', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ExternalCandidate', async () => {
        const returnedFromService = {
          fullName: 'BBBBBB',
          nameReading: 'BBBBBB',
          englishName: 'BBBBBB',
          birthDate: dayjs(currentDate).format(DATE_FORMAT),
          gender: 'BBBBBB',
          phone: 'BBBBBB',
          email: 'BBBBBB',
          address: 'BBBBBB',
          educationSummary: 'BBBBBB',
          workSummary: 'BBBBBB',
          convertedPersonId: 1,
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };

        const expected = { birthDate: currentDate, createdAt: currentDate, updatedAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a ExternalCandidate', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ExternalCandidate', async () => {
        const patchObject = {
          email: 'BBBBBB',
          educationSummary: 'BBBBBB',
          convertedPersonId: 1,
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...new ExternalCandidate(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { birthDate: currentDate, createdAt: currentDate, updatedAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ExternalCandidate', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ExternalCandidate', async () => {
        const returnedFromService = {
          fullName: 'BBBBBB',
          nameReading: 'BBBBBB',
          englishName: 'BBBBBB',
          birthDate: dayjs(currentDate).format(DATE_FORMAT),
          gender: 'BBBBBB',
          phone: 'BBBBBB',
          email: 'BBBBBB',
          address: 'BBBBBB',
          educationSummary: 'BBBBBB',
          workSummary: 'BBBBBB',
          convertedPersonId: 1,
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          updatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { birthDate: currentDate, createdAt: currentDate, updatedAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of ExternalCandidate', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ExternalCandidate', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ExternalCandidate', async () => {
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
