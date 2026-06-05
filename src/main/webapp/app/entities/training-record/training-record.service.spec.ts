import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { TrainingRecord } from '@/shared/model/training-record.model';

import TrainingRecordService from './training-record.service';

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
  describe('TrainingRecord Service', () => {
    let service: TrainingRecordService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new TrainingRecordService();
      currentDate = new Date();
      elemDefault = new TrainingRecord(123, currentDate, 'ONBOARDING', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { trainingDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
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

      it('should create a TrainingRecord', async () => {
        const returnedFromService = { id: 123, trainingDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { trainingDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a TrainingRecord', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a TrainingRecord', async () => {
        const returnedFromService = {
          trainingDate: dayjs(currentDate).format(DATE_FORMAT),
          trainingType: 'BBBBBB',
          topic: 'BBBBBB',
          taskDescription: 'BBBBBB',
          resultDescription: 'BBBBBB',
          evidence: 'BBBBBB',
          nextAction: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { trainingDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a TrainingRecord', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a TrainingRecord', async () => {
        const patchObject = {
          trainingDate: dayjs(currentDate).format(DATE_FORMAT),
          trainingType: 'BBBBBB',
          topic: 'BBBBBB',
          resultDescription: 'BBBBBB',
          ...new TrainingRecord(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { trainingDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a TrainingRecord', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of TrainingRecord', async () => {
        const returnedFromService = {
          trainingDate: dayjs(currentDate).format(DATE_FORMAT),
          trainingType: 'BBBBBB',
          topic: 'BBBBBB',
          taskDescription: 'BBBBBB',
          resultDescription: 'BBBBBB',
          evidence: 'BBBBBB',
          nextAction: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { trainingDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of TrainingRecord', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a TrainingRecord', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a TrainingRecord', async () => {
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
